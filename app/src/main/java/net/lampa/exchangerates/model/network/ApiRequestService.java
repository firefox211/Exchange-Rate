package net.lampa.exchangerates.model.network;

import net.lampa.exchangerates.model.entities.HistoryResponseEntity;
import net.lampa.exchangerates.model.entities.LatestRateResponseEntity;

import io.reactivex.Single;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiRequestService {

    @GET(NetworkUrls.LATEST)
    Single<Response<LatestRateResponseEntity>> getLatestRates(@Query("base") String baseCurrency);

    @GET(NetworkUrls.HISTORY)
    Single<Response<HistoryResponseEntity>> getHistory(@Query("base") String rateFromName,
                                                       @Query("symbols") String rateToName,
                                                       @Query("start_at") String dateFrom,
                                                       @Query("end_at") String dateTo);
}
