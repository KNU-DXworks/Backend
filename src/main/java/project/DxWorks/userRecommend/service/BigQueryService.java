package project.DxWorks.userRecommend.service;

import com.google.cloud.bigquery.BigQuery;
import com.google.cloud.bigquery.BigQueryOptions;
import com.google.cloud.bigquery.InsertAllRequest;
import com.google.cloud.bigquery.TableId;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import project.DxWorks.userRecommend.dto.TransformationRecordDto;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class BigQueryService {

    @Value("${gcp.project-id}")
    private String projectId;

    private BigQuery bigQuery;

    @PostConstruct
    public void init() {
        this.bigQuery = BigQueryOptions.newBuilder()
                .setProjectId(projectId)
                .build()
                .getService();
    }

    public void saveUserTransformation(TransformationRecordDto dto) {
        TableId tableId = TableId.of("dxworks-rag-ai", "recommendation_data", "user_transformation");

        Map<String, Object> row = Map.of(
                "user_id", dto.getUserId(),
                "nickname", dto.getUsername(),
                "start_group", dto.getStartGroup(),
                "end_group", dto.getEndGroup(),
                "weight_end", dto.getWeightEnd(),
                "muscle_end", dto.getMuscleEnd(),
                "fat_end", dto.getFatEnd(),
                "completed_at", dto.getCompletedAt().toString()
        );

        InsertAllRequest request = InsertAllRequest.newBuilder(tableId)
                .addRow(row)
                .build();

        bigQuery.insertAll(request);
    }
}
