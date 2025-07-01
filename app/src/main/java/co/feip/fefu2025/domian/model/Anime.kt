package co.feip.fefu2025.domain.model
import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class Anime(
    val id: Int,
    val title: String,
    val description: String,
    val genres: List<String>,
    val rating: Double,
    val year: Int,
    val episodes: Int,
    val imageResId: Int, // локальный ресурс, можно оставить или убрать
    val imageUrl: String, // добавь это поле для URL картинки
    val ratingsDistribution: Map<Int, Int> = emptyMap()
) : Parcelable