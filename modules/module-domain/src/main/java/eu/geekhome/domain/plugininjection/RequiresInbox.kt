package eu.geekhome.domain.plugininjection

import eu.geekhome.domain.inbox.Inbox

interface RequiresInbox : AllFeaturesInjectedListener {
    fun injectInbox(inbox: Inbox)
}