package me.horyu.kkutuweb.result

data class ActionResult(
    val success: Boolean,
    val resultCode: String
) {
    companion object {
        fun success(): ActionResult {
            return ActionResult(success = true, resultCode = RestResult.SUCCESS.name)
        }
    }
}