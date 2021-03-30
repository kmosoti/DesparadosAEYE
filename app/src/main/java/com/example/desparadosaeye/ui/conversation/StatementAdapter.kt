package com.example.desparadosaeye.ui.conversation

import android.R.attr.label
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.view.*
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.recyclerview.widget.RecyclerView
import com.example.desparadosaeye.R
import com.example.desparadosaeye.data.ApplicationModel
import com.example.desparadosaeye.data.Statement
import com.example.desparadosaeye.data.StatementOrigin


class StatementAdapter(
    private val applicationModel: ApplicationModel
) :
    RecyclerView.Adapter<StatementAdapter.StatementViewHolder>() {

    private val statements: MutableList<Statement>
        get() = applicationModel.statements

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatementViewHolder {
        val resId = when(viewType) {
            StatementOrigin.USER.ordinal -> R.layout.user_statement
            StatementOrigin.AI.ordinal -> R.layout.ai_statement
            else -> throw NotImplementedError("viewType must be either USER or AI")
        }

        val itemView = LayoutInflater.from(parent.context)
            .inflate(resId, parent, false)
        return StatementViewHolder(applicationModel, itemView)
    }

    override fun onBindViewHolder(holder: StatementViewHolder, position: Int) {
        val statement = statements[position]
        holder.bind(statement)
    }

    override fun getItemCount(): Int {
        return statements.size
    }

    override fun getItemViewType(position: Int): Int {
        return statements[position].origin.ordinal
    }

    class StatementViewHolder(
        val applicationModel: ApplicationModel,
        val itemView: View
    ) :
        RecyclerView.ViewHolder(itemView) {

        private val messageTextView = itemView.findViewById<TextView>(R.id.message_text_view)
        private var currentStatement: Statement? = null

        init {
            itemView.setOnLongClickListener { showOptionsPopupMenu() }
        }

        private fun showOptionsPopupMenu(): Boolean {
            val popupMenu = PopupMenu(itemView.context, itemView)
            popupMenu.menuInflater.inflate(R.menu.menu_statement, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener {
                onPopupMenuItemSelected(it)
            }
            popupMenu.show()
            return true

            // capture the position so the recycler view knows where to put the context menu
            //statementAdapter.currentLongPressedStatementViewHolder = this
            //return false // let recyclerView handle the context menu creation
        }

        private fun onPopupMenuItemSelected(item: MenuItem): Boolean {
            // Handle item selection
            return when (item.itemId) {
                R.id.copy_item -> {
                    val clipboardService: ClipboardManager =
                        itemView.context.getSystemService(Context.CLIPBOARD_SERVICE)
                        as ClipboardManager
                    val clip = ClipData.newPlainText(
                        itemView.context.getString(R.string.clipboard_label_message_content),
                        currentStatement!!.content)
                    clipboardService.setPrimaryClip(clip)

                    Toast.makeText(
                        itemView.context,
                        itemView.context.getString(R.string.message_copied),
                        Toast.LENGTH_SHORT
                    ).show()

                    true
                }
                R.id.delete_item -> {
                    applicationModel.removeStatement(currentStatement!!)

                    Toast.makeText(
                        itemView.context,
                        itemView.context.getString(R.string.message_deleted),
                        Toast.LENGTH_SHORT
                    ).show()

                    true
                }
                else -> throw NotImplementedError("popup menu item other than copy or delete selected")
            }
        }

        fun bind(statement: Statement) {
            currentStatement = statement
            messageTextView.text = currentStatement!!.content
        }
    }
}