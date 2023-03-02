package com.arsars.pixabayclient.domain.usecases

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

abstract class UseCase<Input, Output> (
    private val dispatcher: CoroutineDispatcher
) {

    suspend operator fun invoke(input: Input): Output {
        return withContext(dispatcher) {
            doWork(input)
        }
    }

    abstract suspend fun doWork(input: Input): Output
}