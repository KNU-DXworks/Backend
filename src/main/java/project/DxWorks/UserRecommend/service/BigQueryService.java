package project.DxWorks.UserRecommend.service;

import com.google.cloud.bigquery.*;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.View;
import project.DxWorks.UserRecommend.dto.EmbeddingRequestDto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

@Service
public class BigQueryService {

    private final BigQuery bigQuery = BigQueryOptions.getDefaultInstance().getService();
    private final String datasetName = "inbody_vectors";
    private final String tableName = "embedding_vector";
    private final View error;

    public BigQueryService(View error) {
        this.error = error;
    }

    public void saveEmbedding(long userId, EmbeddingRequestDto dto){
        Map<String, Object> rowContent = new HashMap<>();
        rowContent.put("userId", userId);
        rowContent.put("created_time", dto.vector().get(0));
        rowContent.put("gender_encoded", dto.vector().get(1));
        rowContent.put("height", dto.vector().get(2));
        rowContent.put("weight", dto.vector().get(3));
        rowContent.put("muscle", dto.vector().get(4));
        rowContent.put("fat", dto.vector().get(5));
        rowContent.put("bmi", dto.vector().get(6));
        rowContent.put("body_type_encoded", dto.vector().get(7));
        rowContent.put("arm_grade_encoded", dto.vector().get(8));
        rowContent.put("body_grade_encoded", dto.vector().get(9));
        rowContent.put("leg_grade_encoded", dto.vector().get(10));


        //BigQuery에 실제로 데이터를 저장하는 요청(insertAll) 실행해야함.
        //TODO : BIGQUERY 유료 계정 업그레이드 해야함.(Streaming insert x)
        InsertAllRequest insertAllRequest = InsertAllRequest.newBuilder(datasetName, tableName)
                .addRow(rowContent).build();

        InsertAllResponse response = bigQuery.insertAll(insertAllRequest);

        if(response.hasErrors()){
            response.getInsertErrors().forEach((key,error) -> {
                System.err.println("삽입 중 오류 발생" + key + ": " + error);
            });
        }
        throw new RuntimeException("BigQuery insert failed.");
    }
}
