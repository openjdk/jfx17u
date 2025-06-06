/*
 * Copyright (C) 2023 Apple Inc. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY APPLE INC. ``AS IS'' AND ANY
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL APPLE INC. OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY
 * OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

#include "config.h"
#include "CSSUnresolvedColor.h"

#include "StyleBuilderState.h"
#include "StyleColor.h"

namespace WebCore {

CSSUnresolvedColor::~CSSUnresolvedColor() = default;

bool CSSUnresolvedColor::containsCurrentColor() const
{
    return WTF::switchOn(m_value, [](auto& unresolved) { return WebCore::containsCurrentColor(unresolved); });
}

bool CSSUnresolvedColor::containsColorSchemeDependentColor() const
{
    return WTF::switchOn(m_value, [](auto& unresolved) { return WebCore::containsColorSchemeDependentColor(unresolved); });
}

void CSSUnresolvedColor::serializationForCSS(StringBuilder& builder) const
{
    return WTF::switchOn(m_value, [&](auto& unresolved) { WebCore::serializationForCSS(builder, unresolved); });
}

String CSSUnresolvedColor::serializationForCSS() const
{
    return WTF::switchOn(m_value, [](auto& unresolved) -> String { return WebCore::serializationForCSS(unresolved); });
}

bool CSSUnresolvedColor::equals(const CSSUnresolvedColor& other) const
{
    return m_value == other.m_value;
}

StyleColor CSSUnresolvedColor::createStyleColor(const Document& document, RenderStyle& style, Style::ForVisitedLink forVisitedLink) const
{
    return WTF::switchOn(m_value, [&](auto& unresolved) { return WebCore::createStyleColor(unresolved, document, style, forVisitedLink); });
}

Color CSSUnresolvedColor::createColor(const CSSUnresolvedColorResolutionContext& context) const
{
    return WTF::switchOn(m_value, [&](auto& unresolved) { return WebCore::createColor(unresolved, context); });
}

std::optional<CSSUnresolvedAbsoluteColor> CSSUnresolvedColor::absolute() const
{
    if (auto* absolute = std::get_if<CSSUnresolvedAbsoluteColor>(&m_value))
        return *absolute;
    return std::nullopt;
}

std::optional<CSSUnresolvedColorKeyword> CSSUnresolvedColor::keyword() const
{
    if (auto* keyword = std::get_if<CSSUnresolvedColorKeyword>(&m_value))
        return *keyword;
    return std::nullopt;
}

std::optional<CSSUnresolvedColorHex> CSSUnresolvedColor::hex() const
{
    if (auto* hex = std::get_if<CSSUnresolvedColorHex>(&m_value))
        return *hex;
    return std::nullopt;
}

void serializationForCSS(StringBuilder& builder, const CSSUnresolvedColor& unresolved)
{
    return unresolved.serializationForCSS(builder);
}

String serializationForCSS(const CSSUnresolvedColor& unresolved)
{
    return unresolved.serializationForCSS();
}

bool operator==(const UniqueRef<CSSUnresolvedColor>& a, const UniqueRef<CSSUnresolvedColor>& b)
{
    return a->equals(b.get());
}

} // namespace WebCore
