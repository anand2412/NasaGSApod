package com.gs.apod.view


import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.gs.apod.R
import com.gs.apod.utils.Utils
import com.gs.apod.utils.Utils.bindImageFromUrl
import com.gs.apod.data.Result
import com.gs.apod.databinding.ActivityMainBinding
import com.gs.apod.db.NasaApodEntity
import com.gs.apod.utils.LogUtil
import com.gs.apod.viewmodel.NasaApodViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*


@AndroidEntryPoint
class MainActivity : AppCompatActivity(), View.OnClickListener {

    companion object {
        private const val TAG = "Andrew"
    }

    private lateinit var date: OnDateSetListener
    private var isDatePictureDisplayed: Boolean = false
    private lateinit var binding: ActivityMainBinding
    private var mCalendar = Calendar.getInstance()
    private val nasaApodViewModel: NasaApodViewModel by viewModels()
    private lateinit var nasaApodEntity : NasaApodEntity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        setUpUi(binding)

    }

    private fun setUpUi(binding: ActivityMainBinding) {

        binding.imgFavorite.setOnClickListener(this)
        binding.imgMarkFavorite.setOnClickListener(this)
        date = OnDateSetListener { _, year, month, day ->
            nasaApodViewModel.fetchByDateResult("$year-${month.plus(1)}-$day")
            observeFavoriteUpdate()
        }
        binding.imgCalender.setOnClickListener(this)
        binding.tvDescription.movementMethod = ScrollingMovementMethod.getInstance()

        observeTodayPicture() // get astronomy picture of the day

        binding.imgRefresh.setOnClickListener (this)
        binding.videoView.setOnErrorListener(
            MediaPlayer.OnErrorListener(
            fun(mp: MediaPlayer, what: Int, extra: Int): Boolean {
                Toast.makeText(this@MainActivity, getString(R.string.video_play_error), Toast.LENGTH_SHORT).show()
                return true
            }
        ))
        registerFetchDateObserver()
    }

    private fun registerFetchDateObserver() {
        nasaApodViewModel.fetchByDateResult.observe(this, fun(result: Result<NasaApodEntity>) {
            when (result.status) {
                Result.Status.SUCCESS -> {
                    binding.progressBar.visibility = View.GONE
                    result.data?.let { it ->
                        isDatePictureDisplayed = true
                        bindView(binding, listOf(it)) }
                }
                Result.Status.LOADING -> binding.progressBar.visibility = View.VISIBLE
                Result.Status.ERROR -> {
                    LogUtil.logw(TAG,  result.message!! )
                    binding.progressBar.visibility = View.GONE
                    Snackbar.make(binding.tvDescription, result.message!!, Snackbar.LENGTH_LONG)
                        .show()
                }
            }
        })
    }

    private fun observeFavoriteUpdate() {
        nasaApodViewModel.updateResult.observe(this) {
            if(it == 1) markFavorite()
        }
    }

    private fun observeTodayPicture() {
        nasaApodViewModel.nasaApod.observe(this, fun(result: Result<List<NasaApodEntity>>) {
            if(!isDatePictureDisplayed) {
                updatePictureResult(result)
            }
        })
    }

    /**
     * fetch favorite list and match
     * with currently displayed data
     * if matches then change icon or leave as it is
     */
    private fun markFavorite() {
        nasaApodViewModel.getFavoriteList().observe(this) { entityList ->
            for(entity in entityList) {
                if (nasaApodEntity.date == entity.date) {
                    if (entity.isFavorite == 1) {
                        binding.imgMarkFavorite.setImageResource(R.mipmap.ic_heart_red)
                    } else {
                        binding.imgMarkFavorite.setImageResource(R.mipmap.ic_heart)
                    }
                    break;
                } else {
                    binding.imgMarkFavorite.setImageResource(R.mipmap.ic_heart)
                }
            }
        }
    }

    /**
     * @param result
     *
     */
    private fun updatePictureResult(result: Result<List<NasaApodEntity>>) {
        when (result.status) {
            Result.Status.SUCCESS -> {
                binding.progressBar.visibility = View.GONE
                result.data?.let { bindView(binding, it) }
            }
            Result.Status.LOADING -> binding.progressBar.visibility = View.VISIBLE
            Result.Status.ERROR -> {
                binding.progressBar.visibility = View.GONE
                Snackbar.make(binding.tvDescription, result.message!!, Snackbar.LENGTH_LONG).show()
            }
        }
    }

    /**
     * set picture result to UI
     */
    private fun bindView(binding: ActivityMainBinding, nasaApodEntityList: List<NasaApodEntity>) {
        if (!nasaApodEntityList.isEmpty()) {
            LogUtil.logd(TAG,"bindView "+ nasaApodEntityList[0])
            nasaApodEntity = nasaApodEntityList[0]
            nasaApodEntity.apply {
                setThumbnail()
                binding.tvTitle.text = nasaApodEntity.title
                binding.tvDescription.text = nasaApodEntity.explanation
                binding.tvDate.text = Utils.convertDateFormat(nasaApodEntity.date)
                binding.tvAuthor.text = nasaApodEntity.copyright
                markFavorite()
                binding.tvDescription.scrollTo(0, 0)
            }

            if (Utils.convertStringToDate(Utils.convertDateFormat(nasaApodEntity.date))
                != Utils.convertStringToDate(Utils.getCurrentDate(Calendar.getInstance()))
                && !Utils.isNetworkAvailable(binding.tvDescription.context)) {

                Toast.makeText(
                    binding.tvDescription.context,
                    getString(R.string.last_cache_image),
                    Toast.LENGTH_LONG).show()
            }
        } else if (!Utils.isNetworkAvailable(binding.tvDescription.context)) {
            Toast.makeText(
                binding.tvDescription.context,
                getString(R.string.no_internet),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun setThumbnail(){
        if(nasaApodEntity.mediaType.contentEquals("video")) {
            binding.imgPhoto.visibility = View.GONE
            binding.videoView.visibility = View.VISIBLE
            val uri: Uri = Uri.parse(nasaApodEntity.url)
            binding.videoView.setVideoURI(uri);
        } else {
            binding.imgPhoto.visibility = View.VISIBLE
            binding.videoView.visibility = View.GONE
            bindImageFromUrl(this@MainActivity, binding.imgPhoto, nasaApodEntity.url)
        }
    }

    override fun onClick(view: View?) {
        when(view){
            binding.imgFavorite-> startActivity(Intent(this, FavoriteActivity::class.java))
            binding.imgMarkFavorite -> updateFavoriteInDb()
            binding.imgCalender->openCalendar()
            binding.imgRefresh -> {
                isDatePictureDisplayed = false
                observeTodayPicture()}

        }
    }

    private fun openCalendar() {
        var datePickerDialog = DatePickerDialog(
            this@MainActivity, date,
            mCalendar.get(Calendar.YEAR),
            mCalendar.get(Calendar.MONTH),
            mCalendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.datePicker.maxDate = System.currentTimeMillis()//today's date
        datePickerDialog.datePicker.minDate = 803267303000 //16 June 1995
        datePickerDialog.show()
    }

    private fun updateFavoriteInDb() {
        if(nasaApodEntity != null) {
            if (nasaApodEntity.isFavorite == 0) {
                binding.imgMarkFavorite.setImageResource(R.mipmap.ic_heart_red)
            } else {
                binding.imgMarkFavorite.setImageResource(R.mipmap.ic_heart)
            }
            nasaApodViewModel.updateFavorite(nasaApodEntity, (nasaApodEntity.isFavorite == 0))
        }
    }
}