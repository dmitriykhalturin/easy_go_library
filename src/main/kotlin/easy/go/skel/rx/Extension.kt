package easy.go.skel.rx

import com.google.firebase.crashlytics.FirebaseCrashlytics
import io.reactivex.*
import io.reactivex.disposables.Disposable

/**
 * Created by Dmitriy Khalturin <dmitry.halturin.86@gmail.com>
 * for easy_go_skel on 23.04.20 2:01.
 */

fun <T> ObservableEmitter<T>.use(block: () -> Unit) =
  try {
    block()
  } catch (exception: Exception) {
    FirebaseCrashlytics.getInstance().recordException(exception)
    tryOnError(exception)
  }

fun <T> SingleEmitter<T>.use(block: () -> Unit) =
  try {
    block()
  } catch (exception: Exception) {
    FirebaseCrashlytics.getInstance().recordException(exception)
    tryOnError(exception)
  }

fun <T> MaybeEmitter<T>.use(block: () -> Unit) =
  try {
    block()
  } catch (exception: Exception) {
    FirebaseCrashlytics.getInstance().recordException(exception)
    tryOnError(exception)
  }

fun CompletableEmitter.use(block: () -> Unit) =
  try {
    block()
  } catch (exception: Exception) {
    FirebaseCrashlytics.getInstance().recordException(exception)
    tryOnError(exception)
  }

fun <T> Observable<T>.subscribe(emitter: ObservableEmitter<T>): Disposable {
  return subscribe(emitter::onNext, emitter::tryOnError, emitter::onComplete)
}

fun <T> Single<T>.subscribe(emitter: SingleEmitter<T>): Disposable {
  return subscribe(emitter::onSuccess, emitter::tryOnError)
}

fun <T> Maybe<T>.subscribe(emitter: MaybeEmitter<T>): Disposable {
  return subscribe(emitter::onSuccess, emitter::tryOnError, emitter::onComplete)
}

fun Completable.subscribe(emitter: CompletableEmitter): Disposable {
  return subscribe(emitter::onComplete, emitter::tryOnError)
}
