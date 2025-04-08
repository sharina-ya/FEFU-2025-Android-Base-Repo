package co.feip.fefu2025

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.*
class MainActivity : AppCompatActivity() {

    private lateinit var animeRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        animeRecyclerView = findViewById(R.id.animeRecyclerView)
        animeRecyclerView.layoutManager = GridLayoutManager(this, 2)
        animeRecyclerView.addItemDecoration(
            GridSpacingItemDecoration(
                2,
                resources.getDimensionPixelSize(R.dimen.grid_spacing),
                true
            )
        )

        val animeList = listOf(
            Anime(
                id = 1,
                title = "Наруто",
                description = "История о юном ниндзя Наруто Узумаки, который мечтает стать Хокаге — главой своего селения.",
                genres = listOf("Экшен", "Приключения", "Комедия"),
                rating = 8.5,
                year = 2002,
                episodes = 220,
                imageResId = R.drawable.naruto
            ),
            Anime(
                id = 2,
                title = "Маг целитель",
                description = "История про месть, преодоление и любовь.",
                genres = listOf("Романтика", "Драма", "Комедия"),
                rating = 9.0,
                year = 2022,
                episodes = 12,
                imageResId = R.drawable.aot
            ),
            Anime(
                id = 3,
                title = "Blue lock",
                description = "2018 гoд cтaл пoвopoтным мoмeнтoм для япoнcкoгo футбoлa, кoгдa кoмaндa нe " +
                        "cмoглa пpoйти Чeмпиoнaт миpa, чтo вызвaлo шoк в пpaвитeльcтвeнныx кpугax. Чинoвники пoнимaли, " +
                        "чтo нужнo cpoчнo иcкaть выxoд из этoй кpизиcнoй cитуaции. B peзультaтe был coздaн пpoeкт пoд " +
                        "нaзвaниeм «Cиняя тюpьмa».",
                genres = listOf("Экшен", "Спорт", "Комедия"),
                rating = 8.7,
                year = 2022,
                episodes = 24,
                imageResId = R.drawable.bluelock
            ),
            Anime(
                id = 4,
                title = "Я переродился торговым автоматом",
                description = "Самой распространенной причиной перерождения героя в другом мире становится его смерть под колесами грузовика. Иногда " +
                        "авторы исекаев пытаются креативить. Так, главный герой этого тайтла избежал неудачного перехода через дорогу, но был насмерть " +
                        "придавлен торговым автоматом. Еще более абсурдной ситуацию делает переселение души несчастного паренька в подобный автомат на " +
                        "просторах фэнтэзийного мира. Так бы и стоять ему вечность среди кустов и деревьев, ведь шныряющие вокруг монстры не знают, для чего нужно подобное устройство.",
                genres = listOf("Исэкай", "Фэнтези", "Комедия"),
                rating = 4.1,
                year = 2024,
                episodes = 12,
                imageResId = R.drawable.chto
            ),
        )

        val adapter = AnimeCardAdapter(this, animeList) { anime ->
            val intent = Intent(this, AnimeDetailsActivity::class.java)
            intent.putExtra("anime", anime)
            startActivity(intent)
        }

        animeRecyclerView.adapter = adapter
    }
}