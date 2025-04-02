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
            Game(
                title = "Liquid Puzzle: sort the color",
                imageScreen = "https://staging-images.blacknut.net/62e2aef132113a32e7a9ddef.jpg",
                id = "a3dc9e21-771e-4f79-b001-fdaffb704518",
                type = "CLOUD"
            ),
            Game(
                title = "Super Mombo Quest",
                imageScreen = "https://staging-images.blacknut.net/626a5f21a3e5fc16804d243d.jpg",
                id = "352edd27-9a8f-455b-a417-be2d032998a4",
                type = "CLOUD"
            ),
            Game(
                title = "WARBORN",
                imageScreen = "https://staging-images.blacknut.net/625e8445269e08323919a011.jpg",
                id = "36515c06-7fe6-4ea5-abd6-b52ea23cf386",
                type = "CLOUD"
            ),
            Game(
                title = "Tennis World Tour",
                imageScreen = "https://staging-images.blacknut.net/5c73f374269e0847901e7019.jpg",
                id = "b43d2c9c-bcf9-4df3-bb61-c632bd02ee0d",
                type = "CLOUD"
            ),
            Game(
                title = "LEGO® Indiana Jones™ 2: The Adventure Continues",
                imageScreen = "https://staging-images.blacknut.net/5f314fba2b354b3383510e84.jpg",
                id = "3b83271e-9284-47d4-875b-dc5a2629b9dd",
                type = "CLOUD"
            ),  Game(
            title = "The Smurfs: Mission Vileaf",
            imageScreen = "https://staging-images.blacknut.net/61f95cfba3e5fc0c61d048e6.jpg",
            id = "57ae2e78-04fe-4bb5-92ee-861c42c75cb3",
            type = "CLOUD"
            ),
            Game(
                title = "Tandem: A Tale of Shadows",
                imageScreen = "https://staging-images.blacknut.net/61b2165dec46ca2319883aa5.jpg",
                id = "c30a38a7-af7f-42da-ab04-faa0caf22db0",
                type = "CLOUD"
            ),
            Game(
                title = "My Universe - Green Adventure : Farmer Friends",
                imageScreen = "https://staging-images.blacknut.net/641adf06a3e5fc3cbf9afedf.jpg",
                id = "182a9898-d76f-49d1-ab11-610bd0649d72",
                type = "CLOUD"
            ),
            Game(
                title = "Stunt Kite Party",
                imageScreen = "https://staging-images.blacknut.net/61a75b08b945d64b92b43363.jpg",
                id = "3f133421-17a9-48ea-975b-2274a563d106",
                type = "CLOUD"
            ),
            Game(
                title = "Space Cows",
                imageScreen = "https://staging-images.blacknut.net/61533884b945d60eb591eb24.jpg",
                id = "e64b0776-6131-499a-b9e7-7dd712cca329",
                type = "CLOUD"
            )
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
        activity?.runOnUiThread {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, PlayGameFragment())
                .commit()
        }
    }

    data class Game(val title: String, val imageScreen: String, val id: String, val type:String)

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
                .load(game.imageScreen)
                .placeholder(R.drawable.game_placeholder)
                .into(imageView)
        }

        override fun onUnbindViewHolder(viewHolder: ViewHolder) {
            // Clean up if needed
        }
    }
}