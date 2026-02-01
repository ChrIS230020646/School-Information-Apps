import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.comp_3132sef.data.local.SchoolEntity
import com.example.comp_3132sef.ui.school.SchoolViewModel
import java.util.Locale

@Composable
fun SchoolListCompose(
    viewModel: SchoolViewModel,schools: List<SchoolEntity>
) {
    val favorites by viewModel.favorites.collectAsState()


    val isZh = Locale.getDefault().language == "zh"

    val context = LocalContext.current

    LazyColumn {
        items(schools) { school ->

            val displayName =
                if (isZh) (school.chineseName ?: school.englishName)
                else school.englishName

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { viewModel.openSchoolMap(school) }
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = displayName!!,
                    modifier = Modifier.weight(1f)
                )

                val favKey = school.englishName

                IconButton(onClick = { viewModel.toggleFavorite(favKey) }) {
                    Icon(
                        imageVector =
                            if (favorites.contains(favKey))
                                Icons.Filled.Star
                            else
                                Icons.Outlined.StarBorder,
                        contentDescription = null,
                        tint =
                            if (favorites.contains(favKey))
                                Color(0xFFFFC107)
                            else
                                Color.Gray
                    )
                }
            }
        }
    }




}