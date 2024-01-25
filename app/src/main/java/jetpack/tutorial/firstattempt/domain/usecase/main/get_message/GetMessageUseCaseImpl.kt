package jetpack.tutorial.firstattempt.domain.usecase.main.get_message

import jetpack.tutorial.firstattempt.core.ResultModel
import jetpack.tutorial.firstattempt.domain.data_source.ConversationDataSource
import jetpack.tutorial.firstattempt.domain.model.main.MessageModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetMessageUseCaseImpl @Inject constructor(
    private val dataSource: ConversationDataSource
) : GetMessageUseCase{

    override fun execute(param: GetMessageParam): Flow<ResultModel<MessageModel>> {
        return dataSource.getMessage(param).map {
            when(it) {
                is ResultModel.Success -> {
                    ResultModel.Success(it.result)
                }

                is ResultModel.Error -> {
                    ResultModel.Error(it.t)
                }

                else -> {
                    ResultModel.Error(t = UnknownError())
                }
            }
        }
    }
}