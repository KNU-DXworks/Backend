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
    private final String tableName = "inbody_embeddings";
    private final View error;

    public BigQueryService(View error) {
        this.error = error;
    }


}
