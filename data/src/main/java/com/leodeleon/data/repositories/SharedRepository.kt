package com.leodeleon.data.repositories

import android.content.SharedPreferences
import com.f2prateek.rx.preferences2.Preference
import com.f2prateek.rx.preferences2.RxSharedPreferences
import com.leodeleon.data.utils.SchedulerProvider
import com.leodeleon.domain.entities.*
import com.leodeleon.domain.repositories.ILocalRepository
import com.squareup.moshi.Moshi
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Types
import io.reactivex.Completable
import io.reactivex.Observable

class SharedRepository(private val rxPrefs: RxSharedPreferences, private val sharedPrefs: SharedPreferences):ILocalRepository {

    private companion object {
        const val BOOKMARKS = "BOOKMARKS"
    }

    private val moshi = Moshi.Builder().build()
    private val type = Types.newParameterizedType(List::class.java, Repo::class.java)
    private val adapter: JsonAdapter<List<Repo>> = moshi.adapter(type)

    private val converter = object : Preference.Converter<List<Repo>> {
        override fun deserialize(serialized: String): List<Repo> {
            return adapter.fromJson(serialized)!!
        }

        override fun serialize(value: List<Repo>): String {
           return adapter.toJson(value)
        }
    }

    override fun getBookmarks(): Observable<List<Repo>> {
        return rxPrefs.getObject(BOOKMARKS, emptyList(), converter)
                .asObservable()
                .firstOrError()
                .toObservable()
                .subscribeOn(SchedulerProvider.io())
    }

    override fun saveBookmark(repo: Repo): Completable {
        return getBookmarks().flatMapCompletable {
            val newList = it + repo
            save(converter.serialize(newList))
            Completable.complete()
        }.subscribeOn(SchedulerProvider.io())
    }

    override fun removeBookmark(repo: Repo): Completable {
        return getBookmarks().flatMapCompletable {
            val newList = it.filter { it.id != repo.id }
            save(converter.serialize(newList))
            Completable.complete()
        }.subscribeOn(SchedulerProvider.io())
    }

    private fun save(value: String){
        sharedPrefs.edit().putString(BOOKMARKS,value).apply()
    }
}