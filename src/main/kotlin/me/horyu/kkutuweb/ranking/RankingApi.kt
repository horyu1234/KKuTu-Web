package me.horyu.kkutuweb.ranking

import me.horyu.kkutuweb.ranking.response.RankResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class RankingApi(
        @Autowired private val rankingService: RankingService
) {
    @GetMapping("/ranking")
    fun ranking(@RequestParam(required = false) p: Long?,
                @RequestParam(required = false) id: String?): RankResponse {
        return rankingService.getRanking(p, id)
    }
}