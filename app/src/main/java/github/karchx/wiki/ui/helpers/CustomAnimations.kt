package github.karchx.wiki.ui.helpers

import android.content.Context
import android.view.View
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import github.karchx.wiki.R

class CustomAnimations(val context: Context) {

    private val viewInAnim = AnimationUtils.loadAnimation(context, android.R.anim.fade_in)
    private val viewOutAnim = AnimationUtils.loadAnimation(context, android.R.anim.fade_out)
    private val recyclerAnim = AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation)
    private val clickAnim = AnimationUtils.loadAnimation(context, R.anim.alpha)
    private val longClickAnim = AnimationUtils.loadAnimation(context, R.anim.extra_alpha)

    fun setViewInAnim(vararg views: View) {
        for (view in views) {
            view.animation = viewInAnim
        }
    }

    fun setViewOutAnim(vararg views: View) {
        for (view in views) {
            view.animation = viewOutAnim
        }
    }

    fun setRecyclerAnim(vararg recyclers: RecyclerView) {
        for (recycler in recyclers) {
            recycler.layoutAnimation = recyclerAnim
        }
    }

    fun setClickAnim(vararg views: View) {
        for (view in views) {
            view.startAnimation(clickAnim)
        }
    }

    fun setLongClickAnim(vararg views: View) {
        for (view in views) {
            view.startAnimation(longClickAnim)
        }
    }
}