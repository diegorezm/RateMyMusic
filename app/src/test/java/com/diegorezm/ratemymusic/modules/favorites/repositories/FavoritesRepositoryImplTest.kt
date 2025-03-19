package com.diegorezm.ratemymusic.modules.favorites.repositories

import com.diegorezm.ratemymusic.TestTask
import com.diegorezm.ratemymusic.modules.favorites.domain.repositories.FavoritesRepositoryImpl
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.Mockito.any
import org.mockito.Mockito.eq
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class FavoritesRepositoryImplTest {

    @Mock
    private lateinit var mockFirestore: FirebaseFirestore

    @Mock
    private lateinit var mockDocumentReference: DocumentReference

    @Mock
    private lateinit var mockCollectionReference: CollectionReference

    private lateinit var favoritesRepository: FavoritesRepositoryImpl

    @Before
    fun setUp() {
        favoritesRepository = FavoritesRepositoryImpl(mockFirestore)

        `when`(mockFirestore.collection(anyString())).thenReturn(mockCollectionReference)
        `when`(mockCollectionReference.document(anyString())).thenReturn(mockDocumentReference)
    }

    @Test
    fun `addFavoriteSong should return success when Firestore operation succeeds`() = runBlocking {
        val testTask = TestTask<Void>()
        `when`(mockDocumentReference.update(anyString(), any())).thenReturn(testTask)

        val result = favoritesRepository.addFavoriteTrack("user123", "song456")

        assertTrue(result.isSuccess)
        verify(mockDocumentReference).update(eq("songs"), any(FieldValue::class.java))
    }

    @Test
    fun `addFavoriteSong should return failure when Firestore operation fails`() = runBlocking {
        `when`(
            mockDocumentReference.update(
                anyString(),
                any()
            )
        ).thenThrow(RuntimeException("Firestore error"))

        val result = favoritesRepository.addFavoriteTrack("user123", "song456")

        assertTrue(result.isFailure)
    }

    @Test
    fun `removeFavoriteAlbum should return success when Firestore operation succeeds`() =
        runBlocking {
            val testTask = TestTask<Void>()
            `when`(mockDocumentReference.update(anyString(), any())).thenReturn(
                testTask
            )

            val result = favoritesRepository.removeFavoriteAlbum("user123", "album789")

            assertTrue(result.isSuccess)
            verify(mockDocumentReference).update(eq("albums"), any(FieldValue::class.java))
        }

    @Test
    fun `removeFavoriteAlbum should return failure when Firestore operation fails`() = runBlocking {
        `when`(
            mockDocumentReference.update(
                anyString(),
                any()
            )
        ).thenThrow(RuntimeException("Firestore error"))

        val result = favoritesRepository.removeFavoriteAlbum("user123", "album789")

        assertTrue(result.isFailure)
    }
}