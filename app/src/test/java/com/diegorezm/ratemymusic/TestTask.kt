package com.diegorezm.ratemymusic

import android.app.Activity
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import java.util.concurrent.Executor

class TestTask<T>(
    private val result: T? = null,
    private val exception: Exception? = null,
    private var isComplete: Boolean = false,
    private var isSuccessful: Boolean = false,
    private var isCanceled: Boolean = false
) : Task<T>() {
    private val successListeners: MutableList<OnSuccessListener<in T>> = mutableListOf()
    private val failureListeners: MutableList<OnFailureListener> = mutableListOf()
    private var addedExecutor: Executor? = null
    private var activity: Activity? = null

    override fun addOnFailureListener(listener: OnFailureListener): Task<T> {
        failureListeners.add(listener)
        if (isComplete && !isSuccessful && exception != null) {
            listener.onFailure(exception)
        }
        return this
    }

    override fun addOnFailureListener(activity: Activity, listener: OnFailureListener): Task<T> {
        this.activity = activity
        return addOnFailureListener(listener)
    }

    override fun addOnFailureListener(executor: Executor, listener: OnFailureListener): Task<T> {
        this.addedExecutor = executor
        return addOnFailureListener(listener)
    }

    override fun addOnSuccessListener(listener: OnSuccessListener<in T>): Task<T> {
        successListeners.add(listener)
        if (isComplete && isSuccessful && result != null) {
            listener.onSuccess(result)
        }
        return this
    }

    override fun addOnSuccessListener(
        activity: Activity,
        listener: OnSuccessListener<in T>
    ): Task<T> {
        this.activity = activity
        return addOnSuccessListener(listener)
    }

    override fun addOnSuccessListener(
        executor: Executor,
        listener: OnSuccessListener<in T>
    ): Task<T> {
        this.addedExecutor = executor
        return addOnSuccessListener(listener)
    }

    override fun getException(): Exception? {
        return exception
    }

    override fun getResult(): T? {
        return result
    }

    override fun <X : Throwable> getResult(aClass: Class<X>): T? {
        return result
    }

    override fun isCanceled(): Boolean {
        return isCanceled
    }

    override fun isComplete(): Boolean {
        return isComplete
    }

    override fun isSuccessful(): Boolean {
        return isSuccessful
    }

    fun setComplete(complete: Boolean) {
        isComplete = complete
    }

    fun setSuccessful(successful: Boolean) {
        isSuccessful = successful
    }

    fun setCanceled(canceled: Boolean) {
        isCanceled = canceled
    }

    fun notifySuccess() {
        successListeners.forEach { listener ->
            addedExecutor?.let {
                it.execute {
                    listener.onSuccess(result)
                }
            } ?: run {
                listener.onSuccess(result)
            }

        }
    }

    fun notifyFailure() {
        failureListeners.forEach { listener ->
            addedExecutor?.let {
                it.execute {
                    listener.onFailure(exception!!)
                }
            } ?: run {
                listener.onFailure(exception!!)
            }

        }
    }
}