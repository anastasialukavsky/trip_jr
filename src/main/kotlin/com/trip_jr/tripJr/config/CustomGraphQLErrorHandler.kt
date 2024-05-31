import graphql.GraphQLError

//@Component
//class CustomGraphQLErrorHandler : GraphQLErrorHandler {
//    fun processErrors(errors: List<GraphQLError>): List<GraphQLError> {
//        // Custom error handling logic
//        return errors
//    }
//
//    fun processErrors(throwable: Throwable?): List<GraphQLError> {
//        return java.util.List.of<GraphQLError>(ThrowableGraphQLError(throwable))
//    }
//}