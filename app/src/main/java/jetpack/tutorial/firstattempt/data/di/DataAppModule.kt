package jetpack.tutorial.firstattempt.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import jetpack.tutorial.firstattempt.data.repository.AuthManagerImpl
import jetpack.tutorial.firstattempt.data.repository.AuthRepositoryImpl
import jetpack.tutorial.firstattempt.data.repository.ConversationRepositoryImpl
import jetpack.tutorial.firstattempt.domain.data_source.AuthDataSource
import jetpack.tutorial.firstattempt.domain.data_source.AuthManager
import jetpack.tutorial.firstattempt.domain.data_source.ConversationDataSource

@Module
@InstallIn(ViewModelComponent::class)
abstract class DataAppModule {

    @Binds
    @ViewModelScoped
    abstract fun bindAuthDataSource(impl: AuthRepositoryImpl): AuthDataSource

    @Binds
    @ViewModelScoped
    abstract fun bindAuthManager(impl: AuthManagerImpl): AuthManager

//    @Binds
//    @ViewModelScoped
//    abstract fun bindChatDataSource(impl: ChatRepositoryImpl): ChatDataSource

    @Binds
    @ViewModelScoped
    abstract fun bindConversationSource(impl: ConversationRepositoryImpl): ConversationDataSource
}