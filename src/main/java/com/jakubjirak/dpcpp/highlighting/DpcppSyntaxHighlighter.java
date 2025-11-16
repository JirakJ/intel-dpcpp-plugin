package com.jakubjirak.dpcpp.highlighting;

import com.intellij.lexer.Lexer;
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;

public class DpcppSyntaxHighlighter extends SyntaxHighlighterBase {
    
    public static final TextAttributesKey SYCL_KEYWORD = TextAttributesKey.createTextAttributesKey(
        "DPCPP_SYCL_KEYWORD", DefaultLanguageHighlighterColors.KEYWORD
    );

    @NotNull
    @Override
    public Lexer getHighlightingLexer() {
        // Return empty lexer - C++ highlighting delegated to CLion's built-in support
        return new com.intellij.lexer.EmptyLexer();
    }

    @NotNull
    @Override
    public TextAttributesKey[] getTokenHighlights(IElementType tokenType) {
        return TextAttributesKey.EMPTY_ARRAY;
    }
}
