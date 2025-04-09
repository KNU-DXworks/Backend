package project.DxWorks.GeminiAI.service;

import lombok.RequiredArgsConstructor;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import project.DxWorks.GeminiAI.entity.Inbody;
import project.DxWorks.GeminiAI.repository.InbodyRepository;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;


@Service
@RequiredArgsConstructor
public class InbodyService {

    private final InbodyRepository inbodyRepository;
    private final GeminiService geminiService;

    public Inbody analyzeAndSave(MultipartFile file) throws IOException {

       try{
           //Gemini API 분석한 결과들을 DB 컬럼들에 저장해야함.
           Inbody inbody = geminiService.extractInbodyData(file);

           inbody.setCreatedAt(LocalDateTime.now());

           return inbodyRepository.save(inbody);
       } catch (Exception e) {
           e.printStackTrace(); //전체 에러 출력
           throw new IOException("Gemini 분석 중 오류 발생 , ",e);
       }
    }

    //인바디 데이터 확인
    public List<Inbody> getAll() {
        return inbodyRepository.findAll();
    }
}
