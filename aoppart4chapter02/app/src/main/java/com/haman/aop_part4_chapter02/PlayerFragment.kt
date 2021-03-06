package com.haman.aop_part4_chapter02

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SeekBar
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.haman.aop_part4_chapter02.databinding.FragmentPlayerBinding
import com.haman.aop_part4_chapter02.service.MusicDto
import com.haman.aop_part4_chapter02.service.MusicService
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class PlayerFragment : Fragment(R.layout.fragment_player) {

    private var model: PlayerModel = PlayerModel()
    private var binding: FragmentPlayerBinding? = null
    private lateinit var playListAdapter: PlayListAdapter
    private var player: SimpleExoPlayer? = null

    private val updateSeekRunnable = Runnable {
        updateSeek()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fragmentPlayerBinding = FragmentPlayerBinding.bind(view)
        binding = fragmentPlayerBinding

        initPlayView(fragmentPlayerBinding)
        initPlayListButton(fragmentPlayerBinding)
        initPlayControlButtons(fragmentPlayerBinding)
        initSeekBar(fragmentPlayerBinding)
        initRecyclerView(fragmentPlayerBinding)

        getMusicListFromServer()
    }

    private fun initSeekBar(fragmentPlayerBinding: FragmentPlayerBinding) {
        fragmentPlayerBinding.playerSeekBar.setOnSeekBarChangeListener(object:SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {}
            override fun onStartTrackingTouch(p0: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {
                player?.seekTo((seekBar.progress * 1000).toLong()) // ms ??? ?????? ??????
            }
        })

        fragmentPlayerBinding.playlistSeekBar.setOnTouchListener { view, motionEvent -> false }
    }

    private fun initRecyclerView(fragmentPlayerBinding: FragmentPlayerBinding) {
        playListAdapter = PlayListAdapter {
            // TODO ?????? ??????
            playMusic(it)
        }
        fragmentPlayerBinding.playlistRecyclerView.layoutManager = LinearLayoutManager(context)
        fragmentPlayerBinding.playlistRecyclerView.adapter = playListAdapter
    }

    private fun initPlayListButton(fragmentPlayerBinding: FragmentPlayerBinding) {
        fragmentPlayerBinding.playlistImageView.setOnClickListener {
            // TODO ?????? ???????????? ???????????? ??? ???????????? ?????? ????????? ??????
            if (model.currentPosition == -1) return@setOnClickListener
            fragmentPlayerBinding.playerViewGroup.isVisible = model.isWatchingPlayListView
            fragmentPlayerBinding.playlistViewGroup.isVisible = !model.isWatchingPlayListView
            model.isWatchingPlayListView = !model.isWatchingPlayListView
        }
    }

    private fun initPlayView(fragmentPlayerBinding: FragmentPlayerBinding) {
        context?.let {
            player = SimpleExoPlayer.Builder(it).build()
        }
        fragmentPlayerBinding.playerView.player = player
        binding?.let { binding ->
            player?.addListener(object : Player.EventListener {
                override fun onIsPlayingChanged(isPlaying: Boolean) {
                    super.onIsPlayingChanged(isPlaying)

                    if (isPlaying) { // ?????????
                        binding.playControlImageView.setImageResource(R.drawable.ic_baseline_pause_48)
                    } else {
                        binding.playControlImageView.setImageResource(R.drawable.ic_baseline_play_48)
                    }
                }

                // ???????????? Media ??? ??????????????? ???
                override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                    super.onMediaItemTransition(mediaItem, reason)

                    val newIndex = mediaItem?.mediaId ?: return
                    model.currentPosition = newIndex.toInt()

                    // playView ????????????
                    updatePlayerView(model.currentMusicModel())
                    playListAdapter.submitList(model.getAdapterModels())
                }

                // player ??? ????????? ?????? ???
                override fun onPlaybackStateChanged(playbackState: Int) {
                    super.onPlaybackStateChanged(playbackState)
                    updateSeek()
                }
            })
        }
    }

    private fun updateSeek() {
        val player = this.player ?: return
        val duration = if (player.duration >= 0) player.duration else return // ?????? ???????????? Media ??? ??????
        val position = player.currentPosition // ?????? ?????? ??????

        // TODO UI Update
        updateSeekUi(duration, position)

        // 1??? ?????? updateSeek ?????? handler ??????
        val state = player.playbackState

        view?.removeCallbacks(updateSeekRunnable)
        if (state != Player.STATE_IDLE && state != Player.STATE_ENDED) {
            // ?????? ?????????
            view?.postDelayed(updateSeekRunnable, 1000)
        }
    }

    private fun updateSeekUi(duration: Long, position: Long) {
        binding?.let { binding ->
            // duration : ms
            binding.playlistSeekBar.max = (duration / 1000).toInt()
            binding.playlistSeekBar.progress = (position / 1000).toInt()

            binding.playerSeekBar.max = (duration / 1000).toInt()
            binding.playerSeekBar.progress = (position / 1000).toInt()

            binding.playTimeTextView.text = "%02d:%02d".format(
                TimeUnit.MINUTES.convert(position, TimeUnit.MILLISECONDS),
                (position / 1000) % 60
            )
            binding.totalTimeTextView.text = "%02d:%02d".format(
                TimeUnit.MINUTES.convert(duration, TimeUnit.MILLISECONDS),
                (duration / 1000) % 60
            )
        }
    }

    private fun updatePlayerView(currentMusicModel: MusicModel?) {
        currentMusicModel ?: return

        // view ????????????
        binding?.let { binding ->
            binding.trackTextView.text = currentMusicModel.track
            binding.artistTextView.text = currentMusicModel.artist
            Glide.with(binding.coverImageCardView.context)
                .load(currentMusicModel.coverUrl)
                .into(binding.coverImageView)
        }
    }

    private fun initPlayControlButtons(fragmentPlayerBinding: FragmentPlayerBinding) {
        fragmentPlayerBinding.playControlImageView.setOnClickListener {
            val player = this.player ?: return@setOnClickListener

            if (player.isPlaying) { // ?????????
                player.pause() // -> ????????????
            } else {
                player.play()
            }
        }

        fragmentPlayerBinding.skipNextImageView.setOnClickListener {
            val nextMusic = model.getNextMusic() ?: return@setOnClickListener
            playMusic(nextMusic)
        }
        fragmentPlayerBinding.skipPreviewImageView.setOnClickListener {
            val prevMusic = model.getPrevMusic() ?: return@setOnClickListener
            playMusic(prevMusic)
        }
    }

    private fun getMusicListFromServer() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://run.mocky.io/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        retrofit.create(MusicService::class.java)
            .also {
                it.getListMusics()
                    .enqueue(object : Callback<MusicDto> {
                        override fun onResponse(
                            call: Call<MusicDto>,
                            response: Response<MusicDto>
                        ) {
                            Log.d("PlayerFragment", response.body().toString())
                            response.body()?.let { musicDto ->
                                model = musicDto.mapper()
                                setMusicList(model.getAdapterModels())
                                playListAdapter.submitList(model.getAdapterModels())
                            }
                        }

                        override fun onFailure(call: Call<MusicDto>, t: Throwable) {
                            Log.d("PlayerFragment", t.toString())
                        }
                    })
            }
    }

    private fun setMusicList(musicList: List<MusicModel>) {
        context?.let {
            player?.addMediaItems(musicList.map {
                // MediaItem.fromUri() ??? ??????
                MediaItem.Builder()
                    .setMediaId(it.id.toString()) // ????????? ????????? ??????????????? ?????? ???????????? ?????? ??? ??????
                    .setUri(it.streamUrl).build()
            })
            player?.prepare()
        }
    }

    private fun playMusic(musicModel: MusicModel) {
        // player : MediaItem
        // musicModel
        model.updateCurrentPosition(musicModel)
        player?.seekTo(model.currentPosition, 0)
        player?.play()
    }

    override fun onStop() {
        super.onStop()
        player?.pause()
        view?.removeCallbacks(updateSeekRunnable)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
        player?.release()
        view?.removeCallbacks(updateSeekRunnable)
    }

    companion object {
        fun newInstance(): PlayerFragment {
            return PlayerFragment()
        }
    }
}