package com.example.desparadosaeye.ui.conversation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.desparadosaeye.R

class ConversationFragment : Fragment() {

    private lateinit var conversationViewModel: ConversationViewModel

    private lateinit var submitButton: ImageButton
    lateinit var statementsRecyclerView: RecyclerView
    private lateinit var userMessageEditText: AppCompatEditText

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
        statementsRecyclerView.layoutManager = LinearLayoutManager(context)

        userMessageEditText = view.findViewById(R.id.user_message_edit_text)

        submitButton = view.findViewById(R.id.conversation_submit_button)
        submitButton.setOnClickListener { onClickConversationSubmitButton(it) }

        conversationViewModel.conversationFragment = this
    }

    fun onClickConversationSubmitButton(view: View) {
        conversationViewModel.applicationModel.respondToUserInput(userMessageEditText.text.toString())
        userMessageEditText.text?.clear()
    }
}