package za.ac.sun.grapl.domain.models

open class MethodDescriptorVertex (
        val name: String,
        val typeFullName: String,
        order: Int
): ASTVertex(order)