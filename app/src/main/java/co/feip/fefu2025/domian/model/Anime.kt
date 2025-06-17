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
    val imageResId: Int,
    val ratingsDistribution: Map<Int, Int> = emptyMap() // 1-10: количество оценок
) : Parcelable