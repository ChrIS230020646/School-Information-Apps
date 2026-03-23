import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlin.collections.filterIndexed

class FilterCheckedSet(
    // 這些是 Get (提供給 UI 顯示的清單)
    val sessionList: List<String>,
    val genderList: List<String>,
    val religionList: List<String>,
    val categoryList: List<String>,
    val districtList: List<String>,

    // 這些是 State (控制 Checkbox 的勾選)
    sessionChecked: List<Boolean>,
    genderChecked: List<Boolean>,
    religionChecked: List<Boolean>,
    categoryChecked: List<Boolean>,
    districtChecked: List<Boolean>,

    // 用於內部分發更新的 Callbacks
    private val onUpdate: (String, Int, Boolean) -> Unit
) {
    // 封裝後的狀態 (透過 getter 訪問)
    var sessionChecked by mutableStateOf(sessionChecked)
    var genderChecked by mutableStateOf(genderChecked)
    var religionChecked by mutableStateOf(religionChecked)
    var categoryChecked by mutableStateOf(categoryChecked)
    var districtChecked by mutableStateOf(districtChecked)




    fun getSelectedList(type: String): List<String> {
        return when (type) {
            "sessions" -> sessionList.filterIndexed { i, _ -> sessionChecked[i] }
            "districts" -> districtList.filterIndexed { i, _ -> districtChecked[i] }
            "genders" -> genderList.filterIndexed { i, _ -> genderChecked[i] }
            "religion" -> religionList.filterIndexed { i, _ -> religionChecked[i] }
            "category" -> categoryList.filterIndexed { i, _ -> categoryChecked[i] }
            else -> emptyList()
        }
    }
}