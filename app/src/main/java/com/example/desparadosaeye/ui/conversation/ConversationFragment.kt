package com.example.desparadosaeye.ui.conversation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.desparadosaeye.R

class ConversationFragment : Fragment() {

    private lateinit var conversationViewModel: ConversationViewModel

    private lateinit var statementsRecyclerView: RecyclerView

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

    }
}