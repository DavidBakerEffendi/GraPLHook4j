package za.ac.sun.grapl.domain.mappers

import za.ac.sun.grapl.domain.enums.DispatchTypes
import za.ac.sun.grapl.domain.enums.EvaluationStrategies
import za.ac.sun.grapl.domain.enums.ModifierTypes
import za.ac.sun.grapl.domain.enums.VertexLabels
import za.ac.sun.grapl.domain.enums.VertexLabels.*
import za.ac.sun.grapl.domain.models.GraPLVertex
import za.ac.sun.grapl.domain.models.vertices.*
import kotlin.reflect.KVisibility
import kotlin.reflect.full.memberProperties

class VertexMapper {

    companion object {
        @JvmStatic
        fun propertiesToMap(objectVertex: GraPLVertex): MutableMap<String, Any> {
            val properties = emptyMap<String, Any>().toMutableMap()
            properties["label"] = objectVertex.javaClass.getDeclaredField("LABEL").get(objectVertex).toString()
            objectVertex::class.memberProperties.forEach {
                if (it.visibility == KVisibility.PUBLIC) {
                    if (it.getter.call(objectVertex) is Enum<*>) properties[it.name] = it.getter.call(objectVertex).toString()
                    else properties[it.name] = it.getter.call(objectVertex)!!
                }
            }
            return properties
        }

        @JvmStatic
        fun mapToVertex(map: Map<String, Any>): GraPLVertex {
            when (valueOf(map["label"] as String)) {
                ARRAY_INITIALIZER -> return ArrayInitializerVertex(order = map["order"] as Int)
                BINDING -> return BindingVertex(
                        name = map["name"] as String,
                        signature = map["signature"] as String
                )
                META_DATA -> return MetaDataVertex(
                        language = map["language"] as String,
                        version = map["version"] as String
                )
                FILE -> return FileVertex(
                        name = map["name"] as String,
                        order = map["order"] as Int
                )
                METHOD -> return MethodVertex(
                        name = map["name"] as String,
                        fullName = map["fullName"] as String,
                        signature = map["signature"] as String,
                        lineNumber = map["lineNumber"] as Int,
                        order = map["order"] as Int
                )
                METHOD_PARAMETER_IN -> return MethodParameterInVertex(
                        code = map["code"] as String,
                        name = map["name"] as String,
                        evaluationStrategy = EvaluationStrategies.valueOf(map["evaluationStrategy"] as String),
                        typeFullName = map["typeFullName"] as String,
                        lineNumber = map["lineNumber"] as Int,
                        order = map["order"] as Int
                )
                METHOD_RETURN -> return MethodReturnVertex(
                        name = map["name"] as String,
                        evaluationStrategy = EvaluationStrategies.valueOf(map["evaluationStrategy"] as String),
                        typeFullName = map["typeFullName"] as String,
                        lineNumber = map["lineNumber"] as Int,
                        order = map["order"] as Int
                )
                MODIFIER -> return ModifierVertex(
                        name = ModifierTypes.valueOf(map["name"] as String),
                        order = map["order"] as Int
                )
                TYPE -> return TypeVertex(
                        name = map["name"] as String,
                        fullName = map["fullName"] as String,
                        typeDeclFullName = map["typeDeclFullName"] as String
                )
                TYPE_DECL -> return TypeDeclVertex(
                        name = map["name"] as String,
                        fullName = map["fullName"] as String,
                        typeDeclFullName = map["typeDeclFullName"] as String
                )
                TYPE_PARAMETER -> return TypeParameterVertex(
                        name = map["name"] as String,
                        order = map["order"] as Int
                )
                TYPE_ARGUMENT -> return TypeArgumentVertex(
                        order = map["order"] as Int
                )
                MEMBER -> return MemberVertex(
                        code = map["code"] as String,
                        name = map["name"] as String,
                        typeFullName = map["typeFullName"] as String,
                        order = map["order"] as Int
                )
                NAMESPACE_BLOCK -> return NamespaceBlockVertex(
                        name = map["name"] as String,
                        fullName = map["fullName"] as String,
                        order = map["order"] as Int
                )
                LITERAL -> return LiteralVertex(
                        name = map["name"] as String,
                        typeFullName = map["typeFullName"] as String,
                        lineNumber = map["lineNumber"] as Int,
                        order = map["order"] as Int,
                        argumentIndex = map["argumentIndex"] as Int
                )
                CALL -> return CallVertex(
                        code = map["code"] as String,
                        name = map["name"] as String,
                        typeFullName = map["typeFullName"] as String,
                        order = map["order"] as Int,
                        argumentIndex = map["argumentIndex"] as Int,
                        methodFullName = map["methodFullName"] as String,
                        methodInstFullName = map["methodInstFullName"] as String,
                        lineNumber = map["lineNumber"] as Int,
                        signature = map["signature"] as String,
                        dispatchType = DispatchTypes.valueOf(map["dispatchType"] as String)
                )
                LOCAL -> return LocalVertex(
                        code = map["code"] as String,
                        name = map["name"] as String,
                        typeFullName = map["typeFullName"] as String,
                        lineNumber = map["lineNumber"] as Int,
                        order = map["order"] as Int
                )
                IDENTIFIER -> return IdentifierVertex(
                        code = map["code"] as String,
                        name = map["name"] as String,
                        typeFullName = map["typeFullName"] as String,
                        order = map["order"] as Int,
                        argumentIndex = map["argumentIndex"] as Int,
                        lineNumber = map["lineNumber"] as Int
                )
                FIELD_IDENTIFIER -> return FieldIdentifierVertex(
                        code = map["code"] as String,
                        canonicalName = map["canonicalName"] as String,
                        order = map["order"] as Int,
                        argumentIndex = map["argumentIndex"] as Int,
                        lineNumber = map["lineNumber"] as Int
                )
                RETURN -> return ReturnVertex(
                        code = map["code"] as String,
                        order = map["order"] as Int,
                        argumentIndex = map["argumentIndex"] as Int,
                        lineNumber = map["lineNumber"] as Int
                )
                BLOCK -> return BlockVertex(
                        name = map["name"] as String,
                        typeFullName = map["typeFullName"] as String,
                        order = map["order"] as Int,
                        argumentIndex = map["argumentIndex"] as Int,
                        lineNumber = map["lineNumber"] as Int
                )
                METHOD_REF -> return MethodRefVertex(
                        code = map["code"] as String,
                        order = map["order"] as Int,
                        argumentIndex = map["argumentIndex"] as Int,
                        methodFullName = map["methodFullName"] as String,
                        methodInstFullName = map["methodInstFullName"] as String,
                        lineNumber = map["lineNumber"] as Int
                )
                CONTROL_STRUCTURE -> return ControlStructureVertex(
                        name = map["name"] as String,
                        order = map["order"] as Int,
                        argumentIndex = map["argumentIndex"] as Int,
                        lineNumber = map["lineNumber"] as Int
                )
                UNKNOWN -> return UnknownVertex(
                        code = map["code"] as String,
                        typeFullName = map["typeFullName"] as String,
                        order = map["order"] as Int,
                        argumentIndex = map["argumentIndex"] as Int,
                        lineNumber = map["lineNumber"] as Int
                )
            }
        }
    }

}