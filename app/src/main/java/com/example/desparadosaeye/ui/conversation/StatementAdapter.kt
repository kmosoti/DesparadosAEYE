package com.example.desparadosaeye.ui.conversation

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.desparadosaeye.data.Statement
import com.example.desparadosaeye.data.ApplicationModel
import androidx.recyclerview.widget.RecyclerView
import com.example.desparadosaeye.R
import com.example.desparadosaeye.data.StatementOrigin

class StatementAdapter(private val statements: List<Statement>,
                       private val applicationModel: ApplicationModel) :
    RecyclerView.Adapter<StatementAdapter.StatementViewHolder>() {


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

    class StatementViewHolder(val applicationModel: ApplicationModel, val itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        private val messageTextView = itemView.findViewById<TextView>(R.id.message_text_view)

        private var currentStatement: Statement? = null

        init {
            itemView.setOnLongClickListener { onLongPress() }
        }

        fun onLongPress(): Boolean {
            TODO("give user options to copy or delete message")
        }

        fun bind(statement: Statement) {
            currentStatement = statement
            messageTextView.text = currentStatement!!.content
        }
    }
}