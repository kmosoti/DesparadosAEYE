package com.example.desparadosaeye.ui.conversation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.desparadosaeye.R
import com.google.android.material.textfield.TextInputEditText

class ConversationFragment : Fragment() {

    private lateinit var conversationViewModel: ConversationViewModel

    private lateinit var statementsRecyclerView: RecyclerView
    private lateinit var userMessageEditText: TextInputEditText

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        conversationViewModel =
                ViewModelProvider(this).get(ConversationViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_conversation, container, false)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        statementsRecyclerView = view.findViewById(R.id.statements_recycler_view)
        statementsRecyclerView.adapter = StatementAdapter(conversationViewModel.applicationModel)
        statementsRecyclerView.layoutManager = LinearLayoutManager(context)

        userMessageEditText = view.findViewById(R.id.user_message_edit_text)

        val submitButton = view.findViewById<ImageButton>(R.id.conversation_submit_button)
        submitButton.setOnClickListener { onClickConversationSubmitButton(it) }
    }

    fun onClickConversationSubmitButton(view: View) {
        conversationViewModel.applicationModel.respondToUserInput(userMessageEditText.text.toString())
        userMessageEditText.text?.clear()
    }
}