package za.ac.sun.grapl.domain

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import za.ac.sun.grapl.domain.enums.DispatchTypes
import za.ac.sun.grapl.domain.enums.EvaluationStrategies
import za.ac.sun.grapl.domain.enums.ModifierTypes
import za.ac.sun.grapl.domain.models.GraPLVertex
import za.ac.sun.grapl.domain.models.vertices.*

class ModelTest {
    @Nested
    @DisplayName("Domain model ToString Tests")
    inner class DomainModelToStringTest {
        @Test
        fun arrayInitializerVertexToString() {
            val vertex = ArrayInitializerVertex(0)
            assertEquals("ArrayInitializerVertex{order=0}", vertex.toString())
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
        fun typeParameterVertexToString() {
            val vertex = TypeParameterVertex("TEST", 0)
            assertEquals("TypeParameterVertex{name='TEST', order=0}", vertex.toString())
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

    @Nested
    @DisplayName("Domain model Equal Tests")
    inner class DomainModelEqualTest {

        private fun assertVertexEquality(vertex1: GraPLVertex, vertex2: GraPLVertex, vertex3: GraPLVertex) {
            assertEquals(vertex1, vertex2)
            assertNotEquals(vertex1, vertex3)
            assertNotEquals(vertex1, "TEST")
        }

        @Test
        fun arrayInitializerVertexToString() {
            val vertex1 = ArrayInitializerVertex(0)
            val vertex2 = ArrayInitializerVertex(0)
            val vertex3 = ArrayInitializerVertex(1)
            assertVertexEquality(vertex1, vertex2, vertex3)
        }

        @Test
        fun bindingVertexToString() {
            val vertex1 = BindingVertex("TEST", "I")
            val vertex2 = BindingVertex("TEST", "I")
            val vertex3 = BindingVertex("TEST", "V")
            assertVertexEquality(vertex1, vertex2, vertex3)
        }

        @Test
        fun blockVertexToString() {
            val vertex1 = BlockVertex("TEST", 0, 0, "TEST", 0)
            val vertex2 = BlockVertex("TEST", 0, 0, "TEST", 0)
            val vertex3 = BlockVertex("TEST1", 0, 0, "TEST", 0)
            assertVertexEquality(vertex1, vertex2, vertex3)
        }

        @Test
        fun callVertexToString() {
            val vertex1 = CallVertex("TEST", "TEST", 0, "TEST", "TEST", 0, DispatchTypes.DYNAMIC_DISPATCH, "TEST", "TEST", 0)
            val vertex2 = CallVertex("TEST", "TEST", 0, "TEST", "TEST", 0, DispatchTypes.DYNAMIC_DISPATCH, "TEST", "TEST", 0)
            val vertex3 = CallVertex("TEST1", "TEST", 0, "TEST", "TEST", 0, DispatchTypes.DYNAMIC_DISPATCH, "TEST", "TEST", 0)
            assertVertexEquality(vertex1, vertex2, vertex3)
        }

        @Test
        fun controlStructureVertexToString() {
            val vertex1 = ControlStructureVertex("TEST", 0, 0, 0)
            val vertex2 = ControlStructureVertex("TEST", 0, 0, 0)
            val vertex3 = ControlStructureVertex("TEST1", 0, 0, 0)
            assertVertexEquality(vertex1, vertex2, vertex3)
        }

        @Test
        fun fieldIdentifierVertexToString() {
            val vertex1 = FieldIdentifierVertex("TEST", "TEST", 0, 0, 0)
            val vertex2 = FieldIdentifierVertex("TEST", "TEST", 0, 0, 0)
            val vertex3 = FieldIdentifierVertex("TEST1", "TEST", 0, 0, 0)
            assertVertexEquality(vertex1, vertex2, vertex3)
        }

        @Test
        fun fileVertexToString() {
            val vertex1 = FileVertex("TEST", 0)
            val vertex2 = FileVertex("TEST", 0)
            val vertex3 = FileVertex("TEST1", 0)
            assertVertexEquality(vertex1, vertex2, vertex3)
        }

        @Test
        fun identifierVertexToString() {
            val vertex1 = IdentifierVertex("TEST", "TEST", 0, 0, "TEST", 0)
            val vertex2 = IdentifierVertex("TEST", "TEST", 0, 0, "TEST", 0)
            val vertex3 = IdentifierVertex("TEST1", "TEST", 0, 0, "TEST", 0)
            assertVertexEquality(vertex1, vertex2, vertex3)
        }

        @Test
        fun literalVertexToString() {
            val vertex1 = LiteralVertex("TEST", 0, 0, "TEST", 0)
            val vertex2 = LiteralVertex("TEST", 0, 0, "TEST", 0)
            val vertex3 = LiteralVertex("TEST1", 0, 0, "TEST", 0)
            assertVertexEquality(vertex1, vertex2, vertex3)
        }

        @Test
        fun localVertexToString() {
            val vertex1 = LocalVertex("TEST", "TEST", "TEST", 0, 0)
            val vertex2 = LocalVertex("TEST", "TEST", "TEST", 0, 0)
            val vertex3 = LocalVertex("TEST1", "TEST", "TEST", 0, 0)
            assertVertexEquality(vertex1, vertex2, vertex3)
        }

        @Test
        fun memberVertexToString() {
            val vertex1 = MemberVertex("TEST", "TEST", "TEST", 0)
            val vertex2 = MemberVertex("TEST", "TEST", "TEST", 0)
            val vertex3 = MemberVertex("TEST1", "TEST", "TEST", 0)
            assertVertexEquality(vertex1, vertex2, vertex3)
        }

        @Test
        fun metaDataVertexToString() {
            val vertex1 = MetaDataVertex("TEST", "TEST")
            val vertex2 = MetaDataVertex("TEST", "TEST")
            val vertex3 = MetaDataVertex("TEST1", "TEST")
            assertVertexEquality(vertex1, vertex2, vertex3)
        }

        @Test
        fun methodParameterInVertexToString() {
            val vertex1 = MethodParameterInVertex("TEST", "TEST", EvaluationStrategies.BY_REFERENCE, "TEST", 0, 0)
            val vertex2 = MethodParameterInVertex("TEST", "TEST", EvaluationStrategies.BY_REFERENCE, "TEST", 0, 0)
            val vertex3 = MethodParameterInVertex("TEST1", "TEST", EvaluationStrategies.BY_REFERENCE, "TEST", 0, 0)
            assertVertexEquality(vertex1, vertex2, vertex3)
        }

        @Test
        fun methodRefVertexToString() {
            val vertex1 = MethodRefVertex("TEST", 0, 0, "TEST", "TEST", 0)
            val vertex2 = MethodRefVertex("TEST", 0, 0, "TEST", "TEST", 0)
            val vertex3 = MethodRefVertex("TEST1", 0, 0, "TEST", "TEST", 0)
            assertVertexEquality(vertex1, vertex2, vertex3)
        }

        @Test
        fun methodReturnVertexToString() {
            val vertex1 = MethodReturnVertex("TEST", "TEST", EvaluationStrategies.BY_REFERENCE, 0, 0)
            val vertex2 = MethodReturnVertex("TEST", "TEST", EvaluationStrategies.BY_REFERENCE, 0, 0)
            val vertex3 = MethodReturnVertex("TEST1", "TEST", EvaluationStrategies.BY_REFERENCE, 0, 0)
            assertVertexEquality(vertex1, vertex2, vertex3)
        }

        @Test
        fun methodVertexToString() {
            val vertex1 = MethodVertex("TEST", "TEST", "TEST", 0, 0)
            val vertex2 = MethodVertex("TEST", "TEST", "TEST", 0, 0)
            val vertex3 = MethodVertex("TEST1", "TEST", "TEST", 0, 0)
            assertVertexEquality(vertex1, vertex2, vertex3)
        }

        @Test
        fun modifierVertexToString() {
            val vertex1 = ModifierVertex(ModifierTypes.ABSTRACT, 0)
            val vertex2 = ModifierVertex(ModifierTypes.ABSTRACT, 0)
            val vertex3 = ModifierVertex(ModifierTypes.ABSTRACT, 1)
            assertVertexEquality(vertex1, vertex2, vertex3)
        }

        @Test
        fun namespaceBlockVertexToString() {
            val vertex1 = NamespaceBlockVertex("TEST", "TEST", 0)
            val vertex2 = NamespaceBlockVertex("TEST", "TEST", 0)
            val vertex3 = NamespaceBlockVertex("TEST1", "TEST", 0)
            assertVertexEquality(vertex1, vertex2, vertex3)
        }

        @Test
        fun returnVertexToString() {
            val vertex1 = ReturnVertex(0, 0, 0, "TEST")
            val vertex2 = ReturnVertex(0, 0, 0, "TEST")
            val vertex3 = ReturnVertex(1, 0, 0, "TEST")
            assertVertexEquality(vertex1, vertex2, vertex3)
        }

        @Test
        fun typeArgumentVertexToString() {
            val vertex1 = TypeArgumentVertex(0)
            val vertex2 = TypeArgumentVertex(0)
            val vertex3 = TypeArgumentVertex(1)
            assertVertexEquality(vertex1, vertex2, vertex3)
        }

        @Test
        fun typeDeclVertexToString() {
            val vertex1 = TypeDeclVertex("TEST", "TEST", "TEST")
            val vertex2 = TypeDeclVertex("TEST", "TEST", "TEST")
            val vertex3 = TypeDeclVertex("TEST1", "TEST", "TEST")
            assertVertexEquality(vertex1, vertex2, vertex3)
        }

        @Test
        fun typeParameterVertexToString() {
            val vertex1 = TypeParameterVertex("TEST", 0)
            val vertex2 = TypeParameterVertex("TEST", 0)
            val vertex3 = TypeParameterVertex("TEST1", 0)
            assertVertexEquality(vertex1, vertex2, vertex3)
        }

        @Test
        fun typeVertexToString() {
            val vertex1 = TypeVertex("TEST", "TEST", "TEST")
            val vertex2 = TypeVertex("TEST", "TEST", "TEST")
            val vertex3 = TypeVertex("TEST1", "TEST", "TEST")
            assertVertexEquality(vertex1, vertex2, vertex3)
        }

        @Test
        fun unknownVertexToString() {
            val vertex1 = UnknownVertex("TEST", 0, 0, 0, "TEST")
            val vertex2 = UnknownVertex("TEST", 0, 0, 0, "TEST")
            val vertex3 = UnknownVertex("TEST1", 0, 0, 0, "TEST")
            assertVertexEquality(vertex1, vertex2, vertex3)
        }
    }
}