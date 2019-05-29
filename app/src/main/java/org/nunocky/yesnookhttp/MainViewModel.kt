package org.nunocky.yesnookhttp

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable

class MainViewModel : ViewModel() {
    private val mutableAnswer = MutableLiveData<String>().apply { value = "" }
    private val mutableForced = MutableLiveData<String>().apply { value = "" }
    private val mutableBitmapByteArray = MutableLiveData<ByteArray?>().apply { value = null }

    val answer: LiveData<String> = mutableAnswer
    val forced: LiveData<String> = mutableForced
    val bitmap: LiveData<ByteArray?> = mutableBitmapByteArray

    var view: MainContract.View? = null

    private val compositeDisposable = CompositeDisposable()

    private val yesNoRepository = YesNoRepository()

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    private fun clearInfo() {
        view?.startJudge()

        mutableAnswer.value = "wait a moment..."
        mutableForced.value = ""
        mutableBitmapByteArray.value = null
    }

    fun judge() {
        clearInfo()

        val d = yesNoRepository.getYesNo()
            .subscribe(
                { yesNo ->
                    downloadImage(yesNo)
                },
                { t ->
                    t.printStackTrace()
                })

        compositeDisposable.add(d)
    }

    private fun downloadImage(yesNo: YesNo) {

        val d = yesNoRepository.getImage(yesNo.image)
            .subscribe(
                { bitmapByteArray ->
                    mutableAnswer.value = yesNo.answer
                    mutableForced.value = if (yesNo.forced) "(ﾟ∀ﾟ)" else "(-_-)"
                    mutableBitmapByteArray.value = bitmapByteArray
                    view?.endJudge()
                },
                { t ->
                    t.printStackTrace()
                    view?.endJudge()
                })

        compositeDisposable.add(d)
    }
}

