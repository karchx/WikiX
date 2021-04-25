package github.karchx.wiki.ui.helpers

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

class ViewManager {
    companion object {
        fun hideView(vararg views: View) {
            for (view in views) {
                view.visibility = View.INVISIBLE
            }
        }

        fun showView(vararg views: View) {
            for (view in views) {
                view.visibility = View.VISIBLE
            }
        }

        fun View.hideKeyboard() {
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(windowToken, 0)
        }
    }
}
