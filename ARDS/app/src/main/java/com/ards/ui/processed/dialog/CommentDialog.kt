package com.ards.ui.processed.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Window
import android.widget.Button
import android.widget.EditText
import com.ards.R

class CommentDialog(
    context: Context,
    private val initialComment: String?,
    private val onSave: (String) -> Unit
) : Dialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_edit_fault)

        val etComment: EditText = findViewById(R.id.etFaultInfo)
        val btnSave: Button = findViewById(R.id.btnSave)

        etComment.setText(initialComment)

        btnSave.setOnClickListener {
            val comment = etComment.text.toString().trim()
            onSave(comment)
            dismiss()
        }
    }
}
