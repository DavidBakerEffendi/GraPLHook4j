package za.ac.sun.grapl.domain

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import za.ac.sun.grapl.domain.enums.DispatchTypes
import za.ac.sun.grapl.domain.enums.EvaluationStrategies
import za.ac.sun.grapl.domain.enums.ModifierTypes
import za.ac.sun.grapl.domain.models.vertices.*

class ModelTest {
    @Nested
    @DisplayName("Domain model ToString Tests")
    inner class DomainModelToStringTest {
        @Test
        fun arrayInitializerVertexToString() {
            val vertex = ArrayInitializerVertex()
            assertEquals("ArrayInitializerVertex{}", vertex.toString())
        }

        @Test
        fun bindingVertexToString() {
            val vertex = BindingVertex("TEST", "I")
            assertEquals("BindingVertex{name='TEST', signature='I'}", vertex.toString())
        }

        @Test
        fun blockVertexToString() {
            val vertex = BlockVertex("TEST", 0, 0, "TEST", 0)
            assertEquals("BlockVertex{name='TEST', order=0, argumentIndex=0, typeFullName='TEST', lineNumber=0}", vertex.toString())
        }

        @Test
        fun callVertexToString() {
            val vertex = CallVertex("TEST", "TEST", 0, "TEST", "TEST", 0, DispatchTypes.DYNAMIC_DISPATCH, "TEST", "TEST", 0)
            assertEquals("CallVertex{code='TEST', name='TEST', order=0, methodInstFullName='TEST', methodFullName='TEST', argumentIndex=0, dispatchType=DYNAMIC_DISPATCH, signature='TEST', typeFullName='TEST', lineNumber=0}", vertex.toString())
        }

        @Test
        fun controlStructureVertexToString() {
            val vertex = ControlStructureVertex("TEST", 0, 0, 0)
            assertEquals("ControlStructureVertex{name='TEST', lineNumber=0, order=0, argumentIndex=0}", vertex.toString())
        }

        @Test
        fun fieldIdentifierVertexToString() {
            val vertex = FieldIdentifierVertex("TEST", "TEST", 0, 0, 0)
            assertEquals("FieldIdentifierVertex{code='TEST', canonicalName='TEST', order=0, argumentIndex=0, lineNumber=0}", vertex.toString())
        }

        @Test
        fun fileVertexToString() {
            val vertex = FileVertex("TEST", 0)
            assertEquals("FileVertex{name='TEST', order=0}", vertex.toString())
        }

        @Test
        fun identifierVertexToString() {
            val vertex = IdentifierVertex("TEST", "TEST", 0, 0, "TEST", 0)
            assertEquals("IdentifierVertex{code='TEST', name='TEST', order=0, argumentIndex=0, typeFullName='TEST', lineNumber=0}", vertex.toString())
        }

        @Test
        fun literalVertexToString() {
            val vertex = LiteralVertex("TEST", 0, 0, "TEST", 0)
            assertEquals("LiteralVertex{name='TEST', order=0, argumentIndex=0, typeFullName='TEST', lineNumber=0}", vertex.toString())
        }

        @Test
        fun localVertexToString() {
            val vertex = LocalVertex("TEST", "TEST", "TEST", 0, 0)
            assertEquals("LocalVertex{code='TEST', name='TEST', typeFullName='TEST', lineNumber=0, order=0}", vertex.toString())
        }

        @Test
        fun memberVertexToString() {
            val vertex = MemberVertex("TEST", "TEST", "TEST", 0)
            assertEquals("MemberVertex{code='TEST', name='TEST', typeFullName='TEST', order=0}", vertex.toString())
        }

        @Test
        fun metaDataVertexToString() {
            val vertex = MetaDataVertex("TEST", "TEST")
            assertEquals("MetaDataVertex{language='TEST', version='TEST'}", vertex.toString())
        }

        @Test
        fun methodParameterInVertexToString() {
            val vertex = MethodParameterInVertex("TEST", "TEST", EvaluationStrategies.BY_REFERENCE, "TEST", 0, 0)
            assertEquals("MethodParameterInVertex{code='TEST', name='TEST', evaluationStrategy=BY_REFERENCE, typeFullName='TEST', lineNumber=0, order=0}", vertex.toString())
        }

        @Test
        fun methodRefVertexToString() {
            val vertex = MethodRefVertex("TEST", 0, 0, "TEST", "TEST", 0)
            assertEquals("MethodRefVertex{code='TEST', order=0, argumentIndex=0, methodInstFullName='TEST', methodFullName='TEST', lineNumber=0}", vertex.toString())
        }

        @Test
        fun methodReturnVertexToString() {
            val vertex = MethodReturnVertex("TEST", "TEST", EvaluationStrategies.BY_REFERENCE, 0, 0)
            assertEquals("MethodReturnVertex{name='TEST', evaluationStrategy=BY_REFERENCE, typeFullName='TEST', lineNumber=0, order=0}", vertex.toString())
        }

        @Test
        fun methodVertexToString() {
            val vertex = MethodVertex("TEST", "TEST", "TEST", 0, 0)
            assertEquals("MethodVertex{name='TEST', fullName='TEST', signature='TEST', lineNumber=0, order=0}", vertex.toString())
        }

        @Test
        fun modifierVertexToString() {
            val vertex = ModifierVertex(ModifierTypes.ABSTRACT, 0)
            assertEquals("ModifierVertex{name=ABSTRACT, order=0}", vertex.toString())
        }

        @Test
        fun namespaceBlockVertexToString() {
            val vertex = NamespaceBlockVertex("TEST", "TEST", 0)
            assertEquals("NamespaceBlockVertex{name='TEST', fullName='TEST', order=0}", vertex.toString())
        }

        @Test
        fun returnVertexToString() {
            val vertex = ReturnVertex(0, 0, 0, "TEST")
            assertEquals("ReturnVertex{lineNumber=0, order=0, argumentIndex=0, code='TEST'}", vertex.toString())
        }

        @Test
        fun typeArgumentVertexToString() {
            val vertex = TypeArgumentVertex(0)
            assertEquals("TypeArgumentVertex{order=0}", vertex.toString())
        }

        @Test
        fun typeDeclVertexToString() {
            val vertex = TypeDeclVertex("TEST", "TEST", "TEST")
            assertEquals("TypeDeclVertex{name='TEST', fullName='TEST', typeDeclFullName='TEST'}", vertex.toString())
        }

        @Test
        fun typeVertexToString() {
            val vertex = TypeVertex("TEST", "TEST", "TEST")
            assertEquals("TypeVertex{name='TEST', fullName='TEST', typeDeclFullName='TEST'}", vertex.toString())
        }

        @Test
        fun unknownVertexToString() {
            val vertex = UnknownVertex("TEST", 0, 0, 0, "TEST")
            assertEquals("UnknownVertex{code='TEST', order=0, argumentIndex=0, lineNumber=0, typeFullName='TEST'}", vertex.toString())
        }
    }
}