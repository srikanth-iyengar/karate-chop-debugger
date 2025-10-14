package `in`.srikanthk.devlabs.kchopdebugger.configuration

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.openapi.project.Project
import com.intellij.util.xmlb.annotations.MapAnnotation
import com.intellij.util.xmlb.annotations.Property


@Service(Service.Level.PROJECT)
@State(
    name = "KaratePropertiesState",
    storages = [Storage("KaratePropertiesState.xml")]
)
class KaratePropertiesState: PersistentStateComponent<PropertiesState> {
    private var state = PropertiesState(mutableMapOf())
    override fun getState(): PropertiesState {
        return state
    }

    override fun loadState(newState: PropertiesState) {
        state = newState
    }


    companion object {
        fun getInstance(project: Project): KaratePropertiesState? {
            return project
                .getService(KaratePropertiesState::class.java)
        }
    }

}

data class PropertiesState(
    @Property(surroundWithTag = false)
    @MapAnnotation(entryTagName = "entry", keyAttributeName = "key", valueAttributeName = "value")
    var state: MutableMap<String, String> = mutableMapOf()
)