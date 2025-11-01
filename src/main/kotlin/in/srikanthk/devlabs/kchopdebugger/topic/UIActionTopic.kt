package `in`.srikanthk.devlabs.kchopdebugger.topic

import com.intellij.util.messages.Topic;

interface UIActionTopic {
    companion object {
        val TOPIC: Topic<UIActionTopic> = Topic.create("Karate Chop Debugger Runner", UIActionTopic::class.java)
    }

    fun updateExprText(text: String) { }
}