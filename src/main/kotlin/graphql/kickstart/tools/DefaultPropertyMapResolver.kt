package graphql.kickstart.tools

import com.fasterxml.classmate.TypeResolver
import graphql.kickstart.tools.util.JavaType
import graphql.language.FieldDefinition
import graphql.schema.DataFetcher
import graphql.schema.DataFetchingEnvironment

/**
 * @author Nick Weedon
 *
 * The DefaultPropertyMapResolver implements the Map (i.e. property map) specific portion of the logic within the GraphQL PropertyDataFetcher class.
 */
internal class DefaultPropertyMapResolver(
    field: FieldDefinition,
    search: FieldResolverScanner.Search,
    options: SchemaParserOptions
) : FieldResolver(field, search, options, Any::class.java) {

    override fun createDataFetcher(): DataFetcher<*> {
        return DefaultPropertyMapResolverDataFetcher(getSourceResolver(), field.name)
    }

    override fun scanForMatches(): List<TypeClassMatcher.PotentialMatch> {
        return listOf(TypeClassMatcher.PotentialMatch.returnValue(field.type, Map::class.java, genericType, SchemaClassScanner.FieldTypeReference(field.name), false))
    }

    override fun toString() = "DefaultPropertyMapResolverDataFetcher{key=${field.name}}"
}

internal class DefaultPropertyMapResolverDataFetcher(
    private val sourceResolver: SourceResolver,
    private val key: String
) : DataFetcher<Any> {

    override fun get(environment: DataFetchingEnvironment): Any? {
        val resolvedSourceObject = sourceResolver(environment)
        if (resolvedSourceObject is Map<*, *>) {
            return resolvedSourceObject[key]
        } else {
            throw RuntimeException("DefaultPropertyMapResolverDataFetcher attempt to fetch a field from an object instance that was not a map")
        }
    }
}
