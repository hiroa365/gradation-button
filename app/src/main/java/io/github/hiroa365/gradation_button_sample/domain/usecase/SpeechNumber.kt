package io.github.hiroa365.gradation_button_sample.domain.usecase

import android.app.Application
import android.speech.tts.TextToSpeech
import android.speech.tts.Voice
import android.util.Log
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

interface SpeechNumber {
    suspend operator fun invoke(text: String)
}

class SpeechNumberImpl @Inject constructor(
    application: Application
) : SpeechNumber, TextToSpeech.OnInitListener {

    private var tts: TextToSpeech? = null
    private val locale = Locale.JAPAN

    private var ttsStatus: Int = TextToSpeech.ERROR

    init {
        tts = TextToSpeech(application.applicationContext, ::onInit)
    }

    override suspend operator fun invoke(text: String) = withContext(Dispatchers.IO) {
        if (ttsStatus == TextToSpeech.SUCCESS) {
            tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "utteranceId")
        }
    }

    override fun onInit(status: Int) {
        ttsStatus = status
    }
}


@Module
@InstallIn(SingletonComponent::class)
object SpeechNumberModule {
    @Provides
    @Singleton
    fun provideSpeechNumber(
        application: Application
    ): SpeechNumber {
        return SpeechNumberImpl(application)
    }
}