package me.horyu.kkutuweb.shop

import me.horyu.kkutuweb.shop.response.ShopResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpSession

@RestController
class ShopApi(
        @Autowired private val shopService: ShopService
) {
    @GetMapping("/shop")
    fun getGoods(): ShopResponse {
        return shopService.getGoods()
    }

    @PostMapping("/buy/{id}", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun buyGood(@PathVariable id: String,
                session: HttpSession): String {
        return shopService.buyGood(id, session)
    }

    @PostMapping("/payback/{id}", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun paybackGood(@PathVariable id: String,
                    session: HttpSession): String {
        return shopService.paybackGood(id, session)
    }
}