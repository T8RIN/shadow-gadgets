package com.zedalpha.shadowgadgets.demo

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.core.view.isInvisible
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.zedalpha.shadowgadgets.demo.databinding.ActivityMainBinding
import com.zedalpha.shadowgadgets.demo.topic.ApplicationTopic
import com.zedalpha.shadowgadgets.demo.topic.BehaviorTopic
import com.zedalpha.shadowgadgets.demo.topic.ComposeTopic
import com.zedalpha.shadowgadgets.demo.topic.DrawableTopic
import com.zedalpha.shadowgadgets.demo.topic.IntroTopic
import com.zedalpha.shadowgadgets.demo.topic.IrregularTopic
import com.zedalpha.shadowgadgets.demo.topic.MotionTopic
import com.zedalpha.shadowgadgets.demo.topic.Topic


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val ui = ActivityMainBinding.inflate(layoutInflater)
        setContentView(ui.root)

        ui.contentPager.apply {
            adapter = ContentAdapter(this@MainActivity)
            isUserInputEnabled = false
        }

        ui.infoPager.apply {
            adapter = InfoAdapter()
            isUserInputEnabled = false
        }

        var current = 0
        ui.title.setText(TopicList[0].title)

        fun setTopic(index: Int) {
            if (current == index) return
            ui.infoPager.currentItem = index
            ui.contentPager.currentItem = index
            ui.title.apply {
                setDirection(index > current)
                setText(TopicList[index].title)
            }
            ui.backward.isInvisible = index == 0
            ui.forward.isInvisible = index == TopicList.size - 1
            current = index
        }

        ui.backward.setOnClickListener {
            if (current > 0) setTopic(current - 1)
        }
        ui.forward.setOnClickListener {
            if (current < TopicList.size - 1) setTopic(current + 1)
        }
        ui.title.setOnClickListener { view ->
            PopupMenu(this, view).apply {
                TopicList.forEachIndexed { i, t -> menu.add(0, i, 0, t.title) }
                menu.getItem(current).isEnabled = false
                setOnMenuItemClickListener { setTopic(it.itemId); true }
            }.show()
        }

        if (savedInstanceState == null) {
            val hide = getPreferences(Context.MODE_PRIVATE)
                .getBoolean(PREF_HIDE_WELCOME, false)
            if (!hide) {
                val dialog = AlertDialog.Builder(this)
                    .setView(R.layout.dialog_welcome)
                    .setPositiveButton("Close", null)
                    .show()
                val check =
                    dialog.findViewById<CheckBox>(R.id.check_hide_welcome)
                check?.setOnCheckedChangeListener { _, isChecked ->
                    getPreferences(Context.MODE_PRIVATE).edit()
                        .putBoolean(PREF_HIDE_WELCOME, isChecked)
                        .apply()
                }
            }
        }
    }
}

private val TopicList: List<Topic> = buildList {
    add(IntroTopic)
    add(MotionTopic)
    add(BehaviorTopic)
    if (Build.VERSION.SDK_INT >= 30) add(IrregularTopic)
    add(ApplicationTopic)
    add(DrawableTopic)
    add(ComposeTopic)
}

private class ContentAdapter(activity: FragmentActivity) :
    FragmentStateAdapter(activity) {

    override fun getItemCount() = TopicList.size

    override fun createFragment(position: Int) =
        TopicList[position].createContentFragment()
}

private class InfoAdapter : RecyclerView.Adapter<InfoHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        InfoHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_description, parent, false)
        )

    override fun getItemCount() = TopicList.size

    override fun onBindViewHolder(holder: InfoHolder, position: Int) {
        holder.text.setText(TopicList[position].descriptionResId)
    }
}

private class InfoHolder(view: View) : ViewHolder(view) {

    val text: TextView = view.findViewById(R.id.text)

    init {
        text.setOnLongClickListener {
            AlertDialog.Builder(text.context)
                .setView(R.layout.dialog_description)
                .setPositiveButton("Close", null)
                .show()
                .findViewById<TextView>(R.id.text)?.text = text.text
            true
        }
    }
}

private const val PREF_HIDE_WELCOME = "hide_welcome"