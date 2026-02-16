package com.boaglio.springai.comedyclub;

import org.springframework.ai.audio.transcription.AudioTranscriptionPrompt;
import org.springframework.ai.openai.OpenAiAudioTranscriptionModel;
import org.springframework.ai.openai.OpenAiAudioTranscriptionOptions;
import org.springframework.ai.openai.api.OpenAiAudioApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AudioAPI {

    private final OpenAiAudioTranscriptionModel transcriptionModel;

    @Value("classpath:/audio/reels_casaDoCodigo.mp3")
    private Resource audioFile;

    public AudioAPI(OpenAiAudioTranscriptionModel transcriptionModel) {
        this.transcriptionModel = transcriptionModel;
    }

    @GetMapping("/api/descreva-audio")
    String descreverAudioCasaDoCodigo() {

        var transcriptionOptions = OpenAiAudioTranscriptionOptions.builder()
                .responseFormat(OpenAiAudioApi.TranscriptResponseFormat.TEXT)
                .temperature(0f)
                .build();
        var transcriptionRequest = new AudioTranscriptionPrompt(this.audioFile,
                transcriptionOptions);

        var response = this.transcriptionModel.call(transcriptionRequest);

        return response.getResult().getOutput();
    }

}