package com.example.tv360box

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.leanback.app.RowsSupportFragment
import androidx.leanback.widget.ArrayObjectAdapter
import androidx.leanback.widget.HeaderItem
import androidx.leanback.widget.HorizontalGridView
import androidx.leanback.widget.ItemBridgeAdapter
import androidx.leanback.widget.ListRow
import androidx.leanback.widget.ListRowPresenter
import androidx.leanback.widget.Presenter
import com.bumptech.glide.Glide

class ListGameFragment : RowsSupportFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupGameList()
    }

    private fun setupGameList() {
        val rowsAdapter = ArrayObjectAdapter(ListRowPresenter())

        // Create a row of games
        val gamePresenter = GamePresenter()
        val gameAdapter = ArrayObjectAdapter(gamePresenter)

        // Sample game data
        val games = listOf(
            Game("Game 1", "https://example.com/game1.jpg"),
            Game("Game 2", "https://example.com/game2.jpg"),
            Game("Game 3", "https://example.com/game3.jpg"),
            Game("Game 4", "https://example.com/game4.jpg"),
            Game("Game 5", "https://example.com/game5.jpg"),
            Game("Game 6", "https://example.com/game6.jpg")
        )

        gameAdapter.addAll(0, games)

        // Add the games to a row
        val header = HeaderItem(0, "Games")
        rowsAdapter.add(ListRow(header, gameAdapter))

        adapter = rowsAdapter

        // Set click listener
        setOnItemViewClickedListener { itemViewHolder, item, _, _ ->
            if (item is Game) {
                playGame(item)
            }
        }
    }

    private fun playGame(game: Game) {
        // Implement your game play logic here
    }

    data class Game(val title: String, val imageUrl: String)

    inner class GamePresenter : Presenter() {
        override fun onCreateViewHolder(parent: ViewGroup): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_game, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(viewHolder: ViewHolder, item: Any) {
            val game = item as Game
            val imageView = viewHolder.view.findViewById<ImageView>(R.id.gameImage)
            val titleView = viewHolder.view.findViewById<TextView>(R.id.gameTitle)

            titleView.text = game.title
            Glide.with(viewHolder.view.context)
                .load(game.imageUrl)
                .placeholder(R.drawable.game_placeholder)
                .into(imageView)
        }

        override fun onUnbindViewHolder(viewHolder: ViewHolder) {
            // Clean up if needed
        }
    }
}