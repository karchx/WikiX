package github.karchx.wiki.ui.helpers

import android.content.Context
import android.widget.EditText
import github.karchx.wiki.R

class ViewExceptions {
    companion object {
        fun showEmptyInputFieldError(context: Context, textField: EditText) {
            val errorMessage = context.getString(R.string.empty_field_error)
            textField.error = errorMessage
            textField.requestFocus()
        }

        fun isEmptyInputField(fieldContent: String): Boolean {
            return fieldContent.trim { it <= ' ' }.isEmpty()
        }
    }
}
