package org.nunocky.yesnookhttp

import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class YesNoRepository {

    private val yesNoService = YesNoService()

    fun getYesNo(): Single<YesNo> {
        return Single.create<YesNo> { emitter ->
            try {
                val yesno = yesNoService.getYesNo()

                emitter.onSuccess(yesno)

            } catch (ex: Exception) {
                emitter.onError(ex)
            }
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun getImage(url: String): Single<ByteArray> {
        return Single.create<ByteArray> { emitter ->
            try {
                val byteArray = yesNoService.getImageByteArray(url)
                emitter.onSuccess(byteArray)
            } catch (ex: Exception) {
                emitter.onError(ex)
            }
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}