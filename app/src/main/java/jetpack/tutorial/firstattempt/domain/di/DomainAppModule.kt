package jetpack.tutorial.firstattempt.domain.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import jetpack.tutorial.firstattempt.domain.usecase.auth.auth_state.AuthStateUseCase
import jetpack.tutorial.firstattempt.domain.usecase.auth.auth_state.AuthStateUseCaseImpl
import jetpack.tutorial.firstattempt.domain.usecase.auth.login.LoginUseCase
import jetpack.tutorial.firstattempt.domain.usecase.auth.login.LoginUseCaseImpl
import jetpack.tutorial.firstattempt.domain.usecase.auth.logout.LogoutUseCase
import jetpack.tutorial.firstattempt.domain.usecase.auth.logout.LogoutUseCaseImpl
import jetpack.tutorial.firstattempt.domain.usecase.auth.regsiter.RegisterUseCase
import jetpack.tutorial.firstattempt.domain.usecase.auth.regsiter.RegisterUseCaseImpl
import jetpack.tutorial.firstattempt.domain.usecase.main.check_users_pair.CheckUsersPairUseCase
import jetpack.tutorial.firstattempt.domain.usecase.main.check_users_pair.CheckUsersPairUseCaseImpl
import jetpack.tutorial.firstattempt.domain.usecase.main.get_all_conversations.GetAllConversationsUseCase
import jetpack.tutorial.firstattempt.domain.usecase.main.get_all_conversations.GetAllConversationsUseCaseImpl
import jetpack.tutorial.firstattempt.domain.usecase.main.get_all_messages.GetAllMessagesUseCase
import jetpack.tutorial.firstattempt.domain.usecase.main.get_all_messages.GetAllMessagesUseCaseImpl
import jetpack.tutorial.firstattempt.domain.usecase.main.get_conversation.GetConversationUseCase
import jetpack.tutorial.firstattempt.domain.usecase.main.get_conversation.GetConversationUseCaseImpl
import jetpack.tutorial.firstattempt.domain.usecase.main.get_current_user.GetCurrentUserUseCase
import jetpack.tutorial.firstattempt.domain.usecase.main.get_current_user.GetCurrentUserUseCaseImpl
import jetpack.tutorial.firstattempt.domain.usecase.main.get_message.GetMessageUseCase
import jetpack.tutorial.firstattempt.domain.usecase.main.get_message.GetMessageUseCaseImpl
import jetpack.tutorial.firstattempt.domain.usecase.main.get_user.GetUserUseCase
import jetpack.tutorial.firstattempt.domain.usecase.main.get_user.GetUserUseCaseImpl
import jetpack.tutorial.firstattempt.domain.usecase.main.get_users.GetAllUserUseCase
import jetpack.tutorial.firstattempt.domain.usecase.main.get_users.GetAllUserUseCaseImpl
import jetpack.tutorial.firstattempt.domain.usecase.main.update_conversation.UpdateConversationUseCase
import jetpack.tutorial.firstattempt.domain.usecase.main.update_conversation.UpdateConversationUseCaseImpl
import jetpack.tutorial.firstattempt.domain.usecase.main.update_message.UpdateMessageUseCase
import jetpack.tutorial.firstattempt.domain.usecase.main.update_message.UpdateMessageUseCaseImpl

@Module
@InstallIn(ViewModelComponent::class)
abstract class DomainAppModule {

    @Binds
    @ViewModelScoped
    abstract fun bindLoginUseCase(impl: LoginUseCaseImpl): LoginUseCase

    @Binds
    @ViewModelScoped
    abstract fun bindRegisterUseCase(impl: RegisterUseCaseImpl): RegisterUseCase

    @Binds
    @ViewModelScoped
    abstract fun bindLogoutUseCase(impl: LogoutUseCaseImpl): LogoutUseCase

    @Binds
    @ViewModelScoped
    abstract fun bindAuthStateUseCase(impl: AuthStateUseCaseImpl): AuthStateUseCase

    @Binds
    @ViewModelScoped
    abstract fun bindGetAllUserUseCase(impl: GetAllUserUseCaseImpl): GetAllUserUseCase

    @Binds
    @ViewModelScoped
    abstract fun bindGetUserUseCase(impl: GetUserUseCaseImpl): GetUserUseCase

    @Binds
    @ViewModelScoped
    abstract fun bindGetCurrentUserUseCase(impl: GetCurrentUserUseCaseImpl): GetCurrentUserUseCase

    @Binds
    @ViewModelScoped
    abstract fun bindUpdateConversationUseCase(impl: UpdateConversationUseCaseImpl): UpdateConversationUseCase

    @Binds
    @ViewModelScoped
    abstract fun bindUpdateMessageUseCase(impl: UpdateMessageUseCaseImpl): UpdateMessageUseCase

    @Binds
    @ViewModelScoped
    abstract fun bindCheckUsersPairUseCase(impl: CheckUsersPairUseCaseImpl): CheckUsersPairUseCase

    @Binds
    @ViewModelScoped
    abstract fun bindGetAllConversationsUseCase(impl: GetAllConversationsUseCaseImpl): GetAllConversationsUseCase

    @Binds
    @ViewModelScoped
    abstract fun bindGetConversationUseCase(impl: GetConversationUseCaseImpl): GetConversationUseCase

    @Binds
    @ViewModelScoped
    abstract fun bindGetAllMessagesUseCase(impl: GetAllMessagesUseCaseImpl): GetAllMessagesUseCase

    @Binds
    @ViewModelScoped
    abstract fun bindGetMessageUseCase(impl: GetMessageUseCaseImpl): GetMessageUseCase
}