package com.ramon.ramondrive.presentation.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color
import com.ramon.ramondrive.domain.model.AppTheme

// Generated from the official OpenCode theme definitions (each theme ships a dark and a light palette).
// Palettes are not meant to be edited by hand; regenerate from the source JSON when updating.

internal data class OpenCodePalette(
    val neutral: Color,
    val ink: Color,
    val primary: Color,
    val accent: Color,
    val success: Color,
    val warning: Color,
    val error: Color,
    val info: Color,
    val diffAdd: Color,
    val diffDelete: Color,
)

internal class OpenCodeThemeSpec(
    val id: String,
    val displayName: String,
    val theme: AppTheme,
    internal val lightPalette: OpenCodePalette,
    internal val darkPalette: OpenCodePalette,
)

internal val openCodeThemeSpecs: List<OpenCodeThemeSpec> = listOf(
    OpenCodeThemeSpec("amoled", "AMOLED", AppTheme.OPENCODE_AMOLED,
        lightPalette = OpenCodePalette(neutral = Color(0xFFF0F0F0), ink = Color(0xFF0A0A0A), primary = Color(0xFF6200FF), accent = Color(0xFFFF0080), success = Color(0xFF00E676), warning = Color(0xFFFFAB00), error = Color(0xFFFF1744), info = Color(0xFF00B0FF), diffAdd = Color(0xFF00E676), diffDelete = Color(0xFFFF1744)),
        darkPalette = OpenCodePalette(neutral = Color(0xFF000000), ink = Color(0xFFFFFFFF), primary = Color(0xFFB388FF), accent = Color(0xFFFF4081), success = Color(0xFF00FF88), warning = Color(0xFFFFEA00), error = Color(0xFFFF1744), info = Color(0xFF18FFFF), diffAdd = Color(0xFF00FF88), diffDelete = Color(0xFFFF1744))
    ),
    OpenCodeThemeSpec("aura", "Aura", AppTheme.OPENCODE_AURA,
        lightPalette = OpenCodePalette(neutral = Color(0xFFF5F0FF), ink = Color(0xFF2D2640), primary = Color(0xFFA277FF), accent = Color(0xFFD94F4F), success = Color(0xFF40BF7A), warning = Color(0xFFD9A24A), error = Color(0xFFD94F4F), info = Color(0xFF5BB8D9), diffAdd = Color(0xFFB3E6CC), diffDelete = Color(0xFFF5B3B3)),
        darkPalette = OpenCodePalette(neutral = Color(0xFF15141B), ink = Color(0xFFEDECEE), primary = Color(0xFFA277FF), accent = Color(0xFFFF6767), success = Color(0xFF61FFCA), warning = Color(0xFFFFCA85), error = Color(0xFFFF6767), info = Color(0xFF82E2FF), diffAdd = Color(0xFF61FFCA), diffDelete = Color(0xFFFF6767))
    ),
    OpenCodeThemeSpec("ayu", "Ayu", AppTheme.OPENCODE_AYU,
        lightPalette = OpenCodePalette(neutral = Color(0xFFFDFAF4), ink = Color(0xFF4F5964), primary = Color(0xFF4AA8C8), accent = Color(0xFFEF7D71), success = Color(0xFF5FB978), warning = Color(0xFFEA9F41), error = Color(0xFFE6656A), info = Color(0xFF2F9BCE), diffAdd = Color(0xFFB1D780), diffDelete = Color(0xFFE6656A)),
        darkPalette = OpenCodePalette(neutral = Color(0xFF0F1419), ink = Color(0xFFD6DAE0), primary = Color(0xFF3FB7E3), accent = Color(0xFFF2856F), success = Color(0xFF78D05C), warning = Color(0xFFE4A75C), error = Color(0xFFF58572), info = Color(0xFF66C6F1), diffAdd = Color(0xFF59C57C), diffDelete = Color(0xFFF58572))
    ),
    OpenCodeThemeSpec("carbonfox", "Carbonfox", AppTheme.OPENCODE_CARBONFOX,
        lightPalette = OpenCodePalette(neutral = Color(0xFF8E8E8E), ink = Color(0xFF161616), primary = Color(0xFF0072C3), accent = Color(0xFFDA1E28), success = Color(0xFF198038), warning = Color(0xFFF1C21B), error = Color(0xFFDA1E28), info = Color(0xFF0043CE), diffAdd = Color(0xFF198038), diffDelete = Color(0xFFDA1E28)),
        darkPalette = OpenCodePalette(neutral = Color(0xFF393939), ink = Color(0xFFF2F4F8), primary = Color(0xFF33B1FF), accent = Color(0xFFFF8389), success = Color(0xFF42BE65), warning = Color(0xFFF1C21B), error = Color(0xFFFF8389), info = Color(0xFF78A9FF), diffAdd = Color(0xFF42BE65), diffDelete = Color(0xFFFF8389))
    ),
    OpenCodeThemeSpec("catppuccin-frappe", "Catppuccin Frappe", AppTheme.OPENCODE_CATPPUCCIN_FRAPPE,
        lightPalette = OpenCodePalette(neutral = Color(0xFFEFF1F5), ink = Color(0xFF4C4F69), primary = Color(0xFF7287FD), accent = Color(0xFFD20F39), success = Color(0xFF40A02B), warning = Color(0xFFDF8E1D), error = Color(0xFFD20F39), info = Color(0xFF04A5E5), diffAdd = Color(0xFF40A02B), diffDelete = Color(0xFFD20F39)),
        darkPalette = OpenCodePalette(neutral = Color(0xFF303446), ink = Color(0xFFC6D0F5), primary = Color(0xFF8CAAEE), accent = Color(0xFFF4B8E4), success = Color(0xFFA6D189), warning = Color(0xFFE5C890), error = Color(0xFFE78284), info = Color(0xFF81C8BE), diffAdd = Color(0xFFA6D189), diffDelete = Color(0xFFE78284))
    ),
    OpenCodeThemeSpec("catppuccin-macchiato", "Catppuccin Macchiato", AppTheme.OPENCODE_CATPPUCCIN_MACCHIATO,
        lightPalette = OpenCodePalette(neutral = Color(0xFFEFF1F5), ink = Color(0xFF4C4F69), primary = Color(0xFF7287FD), accent = Color(0xFFD20F39), success = Color(0xFF40A02B), warning = Color(0xFFDF8E1D), error = Color(0xFFD20F39), info = Color(0xFF04A5E5), diffAdd = Color(0xFF40A02B), diffDelete = Color(0xFFD20F39)),
        darkPalette = OpenCodePalette(neutral = Color(0xFF24273A), ink = Color(0xFFCAD3F5), primary = Color(0xFF8AADF4), accent = Color(0xFFF5BDE6), success = Color(0xFFA6DA95), warning = Color(0xFFEED49F), error = Color(0xFFED8796), info = Color(0xFF8BD5CA), diffAdd = Color(0xFFA6DA95), diffDelete = Color(0xFFED8796))
    ),
    OpenCodeThemeSpec("catppuccin", "Catppuccin", AppTheme.OPENCODE_CATPPUCCIN,
        lightPalette = OpenCodePalette(neutral = Color(0xFFEFF1F5), ink = Color(0xFF4C4F69), primary = Color(0xFF7287FD), accent = Color(0xFFD20F39), success = Color(0xFF40A02B), warning = Color(0xFFDF8E1D), error = Color(0xFFD20F39), info = Color(0xFF04A5E5), diffAdd = Color(0xFF40A02B), diffDelete = Color(0xFFD20F39)),
        darkPalette = OpenCodePalette(neutral = Color(0xFF1E1E2E), ink = Color(0xFFCDD6F4), primary = Color(0xFFB4BEFE), accent = Color(0xFFF38BA8), success = Color(0xFFA6E3A1), warning = Color(0xFFF9E2AF), error = Color(0xFFF38BA8), info = Color(0xFF89DCEB), diffAdd = Color(0xFF94E2D5), diffDelete = Color(0xFFF38BA8))
    ),
    OpenCodeThemeSpec("cobalt2", "Cobalt2", AppTheme.OPENCODE_COBALT2,
        lightPalette = OpenCodePalette(neutral = Color(0xFFFFFFFF), ink = Color(0xFF193549), primary = Color(0xFF0066CC), accent = Color(0xFF00ACC1), success = Color(0xFF4CAF50), warning = Color(0xFFFF9800), error = Color(0xFFE91E63), info = Color(0xFFFF5722), diffAdd = Color(0xFF4CAF50), diffDelete = Color(0xFFE91E63)),
        darkPalette = OpenCodePalette(neutral = Color(0xFF193549), ink = Color(0xFFFFFFFF), primary = Color(0xFF0088FF), accent = Color(0xFF2AFFDF), success = Color(0xFF9EFF80), warning = Color(0xFFFFC600), error = Color(0xFFFF0088), info = Color(0xFFFF9D00), diffAdd = Color(0xFFB9FF9F), diffDelete = Color(0xFFFF5FB3))
    ),
    OpenCodeThemeSpec("cursor", "Cursor", AppTheme.OPENCODE_CURSOR,
        lightPalette = OpenCodePalette(neutral = Color(0xFFFCFCFC), ink = Color(0xFF141414), primary = Color(0xFF6F9BA6), accent = Color(0xFF6F9BA6), success = Color(0xFF1F8A65), warning = Color(0xFFDB704B), error = Color(0xFFCF2D56), info = Color(0xFF3C7CAB), diffAdd = Color(0xFF55A583), diffDelete = Color(0xFFE75E78)),
        darkPalette = OpenCodePalette(neutral = Color(0xFF181818), ink = Color(0xFFE4E4E4), primary = Color(0xFF88C0D0), accent = Color(0xFF88C0D0), success = Color(0xFF3FA266), warning = Color(0xFFF1B467), error = Color(0xFFE34671), info = Color(0xFF81A1C1), diffAdd = Color(0xFF70B489), diffDelete = Color(0xFFFC6B83))
    ),
    OpenCodeThemeSpec("dracula", "Dracula", AppTheme.OPENCODE_DRACULA,
        lightPalette = OpenCodePalette(neutral = Color(0xFFF8F8F2), ink = Color(0xFF1F1F2F), primary = Color(0xFF7C6BF5), accent = Color(0xFFD16090), success = Color(0xFF2FBF71), warning = Color(0xFFF7A14D), error = Color(0xFFD9536F), info = Color(0xFF1D7FC5), diffAdd = Color(0xFF9FE3B3), diffDelete = Color(0xFFF8A1B8)),
        darkPalette = OpenCodePalette(neutral = Color(0xFF1D1E28), ink = Color(0xFFF8F8F2), primary = Color(0xFFBD93F9), accent = Color(0xFFFF79C6), success = Color(0xFF50FA7B), warning = Color(0xFFFFB86C), error = Color(0xFFFF5555), info = Color(0xFF8BE9FD), diffAdd = Color(0xFF2FB27D), diffDelete = Color(0xFFFF6B81))
    ),
    OpenCodeThemeSpec("everforest", "Everforest", AppTheme.OPENCODE_EVERFOREST,
        lightPalette = OpenCodePalette(neutral = Color(0xFFFDF6E3), ink = Color(0xFF5C6A72), primary = Color(0xFF8DA101), accent = Color(0xFFDF69BA), success = Color(0xFF8DA101), warning = Color(0xFFF57D26), error = Color(0xFFF85552), info = Color(0xFF35A77C), diffAdd = Color(0xFF4DB380), diffDelete = Color(0xFFF52A65)),
        darkPalette = OpenCodePalette(neutral = Color(0xFF2D353B), ink = Color(0xFFD3C6AA), primary = Color(0xFFA7C080), accent = Color(0xFFD699B6), success = Color(0xFFA7C080), warning = Color(0xFFE69875), error = Color(0xFFE67E80), info = Color(0xFF83C092), diffAdd = Color(0xFFB8DB87), diffDelete = Color(0xFFE26A75))
    ),
    OpenCodeThemeSpec("flexoki", "Flexoki", AppTheme.OPENCODE_FLEXOKI,
        lightPalette = OpenCodePalette(neutral = Color(0xFFFFFCF0), ink = Color(0xFF100F0F), primary = Color(0xFF205EA6), accent = Color(0xFFBC5215), success = Color(0xFF66800B), warning = Color(0xFFBC5215), error = Color(0xFFAF3029), info = Color(0xFF24837B), diffAdd = Color(0xFF66800B), diffDelete = Color(0xFFAF3029)),
        darkPalette = OpenCodePalette(neutral = Color(0xFF100F0F), ink = Color(0xFFCECDC3), primary = Color(0xFFDA702C), accent = Color(0xFF8B7EC8), success = Color(0xFF879A39), warning = Color(0xFFDA702C), error = Color(0xFFD14D41), info = Color(0xFF3AA99F), diffAdd = Color(0xFF879A39), diffDelete = Color(0xFFD14D41))
    ),
    OpenCodeThemeSpec("github", "GitHub", AppTheme.OPENCODE_GITHUB,
        lightPalette = OpenCodePalette(neutral = Color(0xFFFFFFFF), ink = Color(0xFF24292F), primary = Color(0xFF0969DA), accent = Color(0xFF1B7C83), success = Color(0xFF1A7F37), warning = Color(0xFF9A6700), error = Color(0xFFCF222E), info = Color(0xFFBC4C00), diffAdd = Color(0xFF1A7F37), diffDelete = Color(0xFFCF222E)),
        darkPalette = OpenCodePalette(neutral = Color(0xFF0D1117), ink = Color(0xFFC9D1D9), primary = Color(0xFF58A6FF), accent = Color(0xFF39C5CF), success = Color(0xFF3FB950), warning = Color(0xFFE3B341), error = Color(0xFFF85149), info = Color(0xFFD29922), diffAdd = Color(0xFF3FB950), diffDelete = Color(0xFFF85149))
    ),
    OpenCodeThemeSpec("gruvbox", "Gruvbox", AppTheme.OPENCODE_GRUVBOX,
        lightPalette = OpenCodePalette(neutral = Color(0xFFFBF1C7), ink = Color(0xFF3C3836), primary = Color(0xFF076678), accent = Color(0xFF9D0006), success = Color(0xFF79740E), warning = Color(0xFFB57614), error = Color(0xFF9D0006), info = Color(0xFF8F3F71), diffAdd = Color(0xFF79740E), diffDelete = Color(0xFF9D0006)),
        darkPalette = OpenCodePalette(neutral = Color(0xFF282828), ink = Color(0xFFEBDBB2), primary = Color(0xFF83A598), accent = Color(0xFFFB4934), success = Color(0xFFB8BB26), warning = Color(0xFFFABD2F), error = Color(0xFFFB4934), info = Color(0xFFD3869B), diffAdd = Color(0xFFB8BB26), diffDelete = Color(0xFFFB4934))
    ),
    OpenCodeThemeSpec("kanagawa", "Kanagawa", AppTheme.OPENCODE_KANAGAWA,
        lightPalette = OpenCodePalette(neutral = Color(0xFFF2E9DE), ink = Color(0xFF54433A), primary = Color(0xFF2D4F67), accent = Color(0xFFD27E99), success = Color(0xFF6F894E), warning = Color(0xFFCC6D00), error = Color(0xFFE82424), info = Color(0xFF4D699B), diffAdd = Color(0xFF89AF5B), diffDelete = Color(0xFFD61F1F)),
        darkPalette = OpenCodePalette(neutral = Color(0xFF1F1F28), ink = Color(0xFFDCD7BA), primary = Color(0xFF7E9CD8), accent = Color(0xFFD27E99), success = Color(0xFF98BB6C), warning = Color(0xFFD7A657), error = Color(0xFFE82424), info = Color(0xFF76946A), diffAdd = Color(0xFFA9D977), diffDelete = Color(0xFFF24A4A))
    ),
    OpenCodeThemeSpec("lucent-orng", "Lucent Orng", AppTheme.OPENCODE_LUCENT_ORNG,
        lightPalette = OpenCodePalette(neutral = Color(0xFFFFF5F0), ink = Color(0xFF1A1A1A), primary = Color(0xFFEC5B2B), accent = Color(0xFFC94D24), success = Color(0xFF0062D1), warning = Color(0xFFEC5B2B), error = Color(0xFFD1383D), info = Color(0xFF318795), diffAdd = Color(0xFF0062D1), diffDelete = Color(0xFFF52A65)),
        darkPalette = OpenCodePalette(neutral = Color(0xFF2A1A15), ink = Color(0xFFEEEEEE), primary = Color(0xFFEC5B2B), accent = Color(0xFFFFF7F1), success = Color(0xFF6BA1E6), warning = Color(0xFFEC5B2B), error = Color(0xFFE06C75), info = Color(0xFF56B6C2), diffAdd = Color(0xFF6BA1E6), diffDelete = Color(0xFFE26A75))
    ),
    OpenCodeThemeSpec("material", "Material", AppTheme.OPENCODE_MATERIAL,
        lightPalette = OpenCodePalette(neutral = Color(0xFFFAFAFA), ink = Color(0xFF263238), primary = Color(0xFF6182B8), accent = Color(0xFF39ADB5), success = Color(0xFF91B859), warning = Color(0xFFFFB300), error = Color(0xFFE53935), info = Color(0xFFF4511E), diffAdd = Color(0xFF91B859), diffDelete = Color(0xFFE53935)),
        darkPalette = OpenCodePalette(neutral = Color(0xFF263238), ink = Color(0xFFEEFFFF), primary = Color(0xFF82AAFF), accent = Color(0xFF89DDFF), success = Color(0xFFC3E88D), warning = Color(0xFFFFCB6B), error = Color(0xFFF07178), info = Color(0xFFFFCB6B), diffAdd = Color(0xFFC3E88D), diffDelete = Color(0xFFF07178))
    ),
    OpenCodeThemeSpec("matrix", "Matrix", AppTheme.OPENCODE_MATRIX,
        lightPalette = OpenCodePalette(neutral = Color(0xFFEEF3EA), ink = Color(0xFF203022), primary = Color(0xFF1CC24B), accent = Color(0xFFC770FF), success = Color(0xFF1CC24B), warning = Color(0xFFE6FF57), error = Color(0xFFFF4B4B), info = Color(0xFF30B3FF), diffAdd = Color(0xFF5DAC7E), diffDelete = Color(0xFFD53A3A)),
        darkPalette = OpenCodePalette(neutral = Color(0xFF0A0E0A), ink = Color(0xFF62FF94), primary = Color(0xFF2EFF6A), accent = Color(0xFFC770FF), success = Color(0xFF62FF94), warning = Color(0xFFE6FF57), error = Color(0xFFFF4B4B), info = Color(0xFF30B3FF), diffAdd = Color(0xFF77FFAF), diffDelete = Color(0xFFFF7171))
    ),
    OpenCodeThemeSpec("mercury", "Mercury", AppTheme.OPENCODE_MERCURY,
        lightPalette = OpenCodePalette(neutral = Color(0xFFFFFFFF), ink = Color(0xFF363644), primary = Color(0xFF5266EB), accent = Color(0xFF8DA4F5), success = Color(0xFF036E43), warning = Color(0xFFA44200), error = Color(0xFFB0175F), info = Color(0xFF007F95), diffAdd = Color(0xFF036E43), diffDelete = Color(0xFFB0175F)),
        darkPalette = OpenCodePalette(neutral = Color(0xFF171721), ink = Color(0xFFDDDDE5), primary = Color(0xFF8DA4F5), accent = Color(0xFF8DA4F5), success = Color(0xFF77C599), warning = Color(0xFFFC9B6F), error = Color(0xFFFC92B4), info = Color(0xFF77BECF), diffAdd = Color(0xFF77C599), diffDelete = Color(0xFFFC92B4))
    ),
    OpenCodeThemeSpec("monokai", "Monokai", AppTheme.OPENCODE_MONOKAI,
        lightPalette = OpenCodePalette(neutral = Color(0xFFFDF8EC), ink = Color(0xFF292318), primary = Color(0xFFBF7BFF), accent = Color(0xFFD9487C), success = Color(0xFF4FB54B), warning = Color(0xFFF1A948), error = Color(0xFFE54B4B), info = Color(0xFF2D9AD7), diffAdd = Color(0xFFBFE7A3), diffDelete = Color(0xFFF6A3AE)),
        darkPalette = OpenCodePalette(neutral = Color(0xFF272822), ink = Color(0xFFF8F8F2), primary = Color(0xFFAE81FF), accent = Color(0xFFF92672), success = Color(0xFFA6E22E), warning = Color(0xFFFD971F), error = Color(0xFFF92672), info = Color(0xFF66D9EF), diffAdd = Color(0xFF4D7F2A), diffDelete = Color(0xFFF4477C))
    ),
    OpenCodeThemeSpec("nightowl", "Night Owl", AppTheme.OPENCODE_NIGHTOWL,
        lightPalette = OpenCodePalette(neutral = Color(0xFFF0F0F0), ink = Color(0xFF403F53), primary = Color(0xFF4876D6), accent = Color(0xFFAA0982), success = Color(0xFF2AA298), warning = Color(0xFFC96765), error = Color(0xFFDE3D3B), info = Color(0xFF4876D6), diffAdd = Color(0xFF2AA298), diffDelete = Color(0xFFDE3D3B)),
        darkPalette = OpenCodePalette(neutral = Color(0xFF011627), ink = Color(0xFFD6DEEB), primary = Color(0xFF82AAFF), accent = Color(0xFFF78C6C), success = Color(0xFFC5E478), warning = Color(0xFFECC48D), error = Color(0xFFEF5350), info = Color(0xFF82AAFF), diffAdd = Color(0xFFC5E478), diffDelete = Color(0xFFEF5350))
    ),
    OpenCodeThemeSpec("nord", "Nord", AppTheme.OPENCODE_NORD,
        lightPalette = OpenCodePalette(neutral = Color(0xFFECEFF4), ink = Color(0xFF2E3440), primary = Color(0xFF5E81AC), accent = Color(0xFFBF616A), success = Color(0xFF8FBCBB), warning = Color(0xFFD08770), error = Color(0xFFBF616A), info = Color(0xFF81A1C1), diffAdd = Color(0xFFA3BE8C), diffDelete = Color(0xFFBF616A)),
        darkPalette = OpenCodePalette(neutral = Color(0xFF2E3440), ink = Color(0xFFE5E9F0), primary = Color(0xFF88C0D0), accent = Color(0xFFD57780), success = Color(0xFFA3BE8C), warning = Color(0xFFD08770), error = Color(0xFFBF616A), info = Color(0xFF81A1C1), diffAdd = Color(0xFF81A1C1), diffDelete = Color(0xFFBF616A))
    ),
    OpenCodeThemeSpec("oc-2", "OpenCode", AppTheme.OPENCODE_OC_2,
        lightPalette = OpenCodePalette(neutral = Color(0xFFF7F7F7), ink = Color(0xFF171311), primary = Color(0xFFDCDE8D), accent = Color(0xFF034CFF), success = Color(0xFF12C905), warning = Color(0xFFFFDC17), error = Color(0xFFFC533A), info = Color(0xFFA753AE), diffAdd = Color(0xFF9FF29A), diffDelete = Color(0xFFFC533A)),
        darkPalette = OpenCodePalette(neutral = Color(0xFF1F1F1F), ink = Color(0xFFF1ECE8), primary = Color(0xFFFAB283), accent = Color(0xFF034CFF), success = Color(0xFF12C905), warning = Color(0xFFFCD53A), error = Color(0xFFFC533A), info = Color(0xFFEDB2F1), diffAdd = Color(0xFFC8FFC4), diffDelete = Color(0xFFFC533A))
    ),
    OpenCodeThemeSpec("one-dark", "One Dark", AppTheme.OPENCODE_ONE_DARK,
        lightPalette = OpenCodePalette(neutral = Color(0xFFFAFAFA), ink = Color(0xFF383A42), primary = Color(0xFF4078F2), accent = Color(0xFF0184BC), success = Color(0xFF50A14F), warning = Color(0xFFC18401), error = Color(0xFFE45649), info = Color(0xFF986801), diffAdd = Color(0xFF489447), diffDelete = Color(0xFFD65145)),
        darkPalette = OpenCodePalette(neutral = Color(0xFF282C34), ink = Color(0xFFABB2BF), primary = Color(0xFF61AFEF), accent = Color(0xFF56B6C2), success = Color(0xFF98C379), warning = Color(0xFFE5C07B), error = Color(0xFFE06C75), info = Color(0xFFD19A66), diffAdd = Color(0xFFAAD482), diffDelete = Color(0xFFE8828B))
    ),
    OpenCodeThemeSpec("onedarkpro", "One Dark Pro", AppTheme.OPENCODE_ONEDARKPRO,
        lightPalette = OpenCodePalette(neutral = Color(0xFFF5F6F8), ink = Color(0xFF2B303B), primary = Color(0xFF528BFF), accent = Color(0xFFD85462), success = Color(0xFF4FA66D), warning = Color(0xFFD19A66), error = Color(0xFFE06C75), info = Color(0xFF61AFEF), diffAdd = Color(0xFFC2EBCF), diffDelete = Color(0xFFF7C1C5)),
        darkPalette = OpenCodePalette(neutral = Color(0xFF1E222A), ink = Color(0xFFABB2BF), primary = Color(0xFF61AFEF), accent = Color(0xFFE06C75), success = Color(0xFF98C379), warning = Color(0xFFE5C07B), error = Color(0xFFE06C75), info = Color(0xFF56B6C2), diffAdd = Color(0xFF4B815A), diffDelete = Color(0xFFB2555F))
    ),
    OpenCodeThemeSpec("orng", "Orng", AppTheme.OPENCODE_ORNG,
        lightPalette = OpenCodePalette(neutral = Color(0xFFFFFFFF), ink = Color(0xFF1A1A1A), primary = Color(0xFFEC5B2B), accent = Color(0xFFC94D24), success = Color(0xFF0062D1), warning = Color(0xFFEC5B2B), error = Color(0xFFD1383D), info = Color(0xFF318795), diffAdd = Color(0xFF0062D1), diffDelete = Color(0xFFF52A65)),
        darkPalette = OpenCodePalette(neutral = Color(0xFF0A0A0A), ink = Color(0xFFEEEEEE), primary = Color(0xFFEC5B2B), accent = Color(0xFFFFF7F1), success = Color(0xFF6BA1E6), warning = Color(0xFFEC5B2B), error = Color(0xFFE06C75), info = Color(0xFF56B6C2), diffAdd = Color(0xFF6BA1E6), diffDelete = Color(0xFFE26A75))
    ),
    OpenCodeThemeSpec("osaka-jade", "Osaka Jade", AppTheme.OPENCODE_OSAKA_JADE,
        lightPalette = OpenCodePalette(neutral = Color(0xFFF6F5DD), ink = Color(0xFF111C18), primary = Color(0xFF1FAA90), accent = Color(0xFF3D7A52), success = Color(0xFF3D7A52), warning = Color(0xFFB5A020), error = Color(0xFFC7392D), info = Color(0xFF1FAA90), diffAdd = Color(0xFF3D7A52), diffDelete = Color(0xFFC7392D)),
        darkPalette = OpenCodePalette(neutral = Color(0xFF111C18), ink = Color(0xFFC1C497), primary = Color(0xFF2DD5B7), accent = Color(0xFF549E6A), success = Color(0xFF549E6A), warning = Color(0xFFE5C736), error = Color(0xFFFF5345), info = Color(0xFF2DD5B7), diffAdd = Color(0xFF63B07A), diffDelete = Color(0xFFDB9F9C))
    ),
    OpenCodeThemeSpec("palenight", "Palenight", AppTheme.OPENCODE_PALENIGHT,
        lightPalette = OpenCodePalette(neutral = Color(0xFFFAFAFA), ink = Color(0xFF292D3E), primary = Color(0xFF4976EB), accent = Color(0xFF00ACC1), success = Color(0xFF91B859), warning = Color(0xFFFFB300), error = Color(0xFFE53935), info = Color(0xFFF4511E), diffAdd = Color(0xFF91B859), diffDelete = Color(0xFFE53935)),
        darkPalette = OpenCodePalette(neutral = Color(0xFF292D3E), ink = Color(0xFFA6ACCD), primary = Color(0xFF82AAFF), accent = Color(0xFF89DDFF), success = Color(0xFFC3E88D), warning = Color(0xFFFFCB6B), error = Color(0xFFF07178), info = Color(0xFFF78C6C), diffAdd = Color(0xFFC3E88D), diffDelete = Color(0xFFF07178))
    ),
    OpenCodeThemeSpec("rosepine", "Rose Pine", AppTheme.OPENCODE_ROSEPINE,
        lightPalette = OpenCodePalette(neutral = Color(0xFFFAF4ED), ink = Color(0xFF575279), primary = Color(0xFF31748F), accent = Color(0xFFD7827E), success = Color(0xFF286983), warning = Color(0xFFEA9D34), error = Color(0xFFB4637A), info = Color(0xFF56949F), diffAdd = Color(0xFF286983), diffDelete = Color(0xFFB4637A)),
        darkPalette = OpenCodePalette(neutral = Color(0xFF191724), ink = Color(0xFFE0DEF4), primary = Color(0xFF9CCFD8), accent = Color(0xFFEBBCBA), success = Color(0xFF31748F), warning = Color(0xFFF6C177), error = Color(0xFFEB6F92), info = Color(0xFF9CCFD8), diffAdd = Color(0xFF31748F), diffDelete = Color(0xFFEB6F92))
    ),
    OpenCodeThemeSpec("shadesofpurple", "Shades of Purple", AppTheme.OPENCODE_SHADESOFPURPLE,
        lightPalette = OpenCodePalette(neutral = Color(0xFFF7EBFF), ink = Color(0xFF3B2C59), primary = Color(0xFF7A5AF8), accent = Color(0xFFFF6BD5), success = Color(0xFF3DD598), warning = Color(0xFFF7C948), error = Color(0xFFFF6BD5), info = Color(0xFF62D4FF), diffAdd = Color(0xFFC8F8DA), diffDelete = Color(0xFFFFC3EF)),
        darkPalette = OpenCodePalette(neutral = Color(0xFF1A102B), ink = Color(0xFFF5F0FF), primary = Color(0xFFC792FF), accent = Color(0xFFFF7AC6), success = Color(0xFF7BE0B0), warning = Color(0xFFFFD580), error = Color(0xFFFF7AC6), info = Color(0xFF7DD4FF), diffAdd = Color(0xFF53C39F), diffDelete = Color(0xFFD85AA0))
    ),
    OpenCodeThemeSpec("solarized", "Solarized", AppTheme.OPENCODE_SOLARIZED,
        lightPalette = OpenCodePalette(neutral = Color(0xFFFDF6E3), ink = Color(0xFF586E75), primary = Color(0xFF268BD2), accent = Color(0xFFD33682), success = Color(0xFF859900), warning = Color(0xFFB58900), error = Color(0xFFDC322F), info = Color(0xFF2AA198), diffAdd = Color(0xFFC6DC7A), diffDelete = Color(0xFFF2A1A1)),
        darkPalette = OpenCodePalette(neutral = Color(0xFF002B36), ink = Color(0xFF93A1A1), primary = Color(0xFF6C71C4), accent = Color(0xFFD33682), success = Color(0xFF859900), warning = Color(0xFFB58900), error = Color(0xFFDC322F), info = Color(0xFF2AA198), diffAdd = Color(0xFF4C7654), diffDelete = Color(0xFFC34B4B))
    ),
    OpenCodeThemeSpec("synthwave84", "Synthwave '84", AppTheme.OPENCODE_SYNTHWAVE84,
        lightPalette = OpenCodePalette(neutral = Color(0xFFFAFAFA), ink = Color(0xFF262335), primary = Color(0xFF00BCD4), accent = Color(0xFF9C27B0), success = Color(0xFF4CAF50), warning = Color(0xFFFF9800), error = Color(0xFFF44336), info = Color(0xFFFF5722), diffAdd = Color(0xFF4CAF50), diffDelete = Color(0xFFF44336)),
        darkPalette = OpenCodePalette(neutral = Color(0xFF262335), ink = Color(0xFFFFFFFF), primary = Color(0xFF36F9F6), accent = Color(0xFFB084EB), success = Color(0xFF72F1B8), warning = Color(0xFFFEDE5D), error = Color(0xFFFE4450), info = Color(0xFFFF8B39), diffAdd = Color(0xFF97F1D8), diffDelete = Color(0xFFFF5E5B))
    ),
    OpenCodeThemeSpec("tokyonight", "Tokyonight", AppTheme.OPENCODE_TOKYONIGHT,
        lightPalette = OpenCodePalette(neutral = Color(0xFFE1E2E7), ink = Color(0xFF273153), primary = Color(0xFF2E7DE9), accent = Color(0xFFB15C00), success = Color(0xFF587539), warning = Color(0xFF8C6C3E), error = Color(0xFFC94060), info = Color(0xFF007197), diffAdd = Color(0xFF4F8F7B), diffDelete = Color(0xFFD05F7C)),
        darkPalette = OpenCodePalette(neutral = Color(0xFF1A1B26), ink = Color(0xFFC0CAF5), primary = Color(0xFF7AA2F7), accent = Color(0xFFFF9E64), success = Color(0xFF9ECE6A), warning = Color(0xFFE0AF68), error = Color(0xFFF7768E), info = Color(0xFF7DCFFF), diffAdd = Color(0xFF41A6B5), diffDelete = Color(0xFFC34043))
    ),
    OpenCodeThemeSpec("vercel", "Vercel", AppTheme.OPENCODE_VERCEL,
        lightPalette = OpenCodePalette(neutral = Color(0xFFFFFFFF), ink = Color(0xFF171717), primary = Color(0xFF0070F3), accent = Color(0xFF8E4EC6), success = Color(0xFF388E3C), warning = Color(0xFFFF9500), error = Color(0xFFDC3545), info = Color(0xFF0070F3), diffAdd = Color(0xFF46A758), diffDelete = Color(0xFFE5484D)),
        darkPalette = OpenCodePalette(neutral = Color(0xFF000000), ink = Color(0xFFEDEDED), primary = Color(0xFF0070F3), accent = Color(0xFF8E4EC6), success = Color(0xFF46A758), warning = Color(0xFFFFB224), error = Color(0xFFE5484D), info = Color(0xFF52A8FF), diffAdd = Color(0xFF63C46D), diffDelete = Color(0xFFFF6166))
    ),
    OpenCodeThemeSpec("vesper", "Vesper", AppTheme.OPENCODE_VESPER,
        lightPalette = OpenCodePalette(neutral = Color(0xFFF0F0F0), ink = Color(0xFF101010), primary = Color(0xFFFFC799), accent = Color(0xFFB30000), success = Color(0xFF99FFE4), warning = Color(0xFFFFC799), error = Color(0xFFFF8080), info = Color(0xFFFFC799), diffAdd = Color(0xFF99FFE4), diffDelete = Color(0xFFFF8080)),
        darkPalette = OpenCodePalette(neutral = Color(0xFF101010), ink = Color(0xFFFFFFFF), primary = Color(0xFFFFC799), accent = Color(0xFFFF8080), success = Color(0xFF99FFE4), warning = Color(0xFFFFC799), error = Color(0xFFFF8080), info = Color(0xFFFFC799), diffAdd = Color(0xFF99FFE4), diffDelete = Color(0xFFFF8080))
    ),
    OpenCodeThemeSpec("zenburn", "Zenburn", AppTheme.OPENCODE_ZENBURN,
        lightPalette = OpenCodePalette(neutral = Color(0xFFFFFFEF), ink = Color(0xFF3F3F3F), primary = Color(0xFF5F7F8F), accent = Color(0xFF5F8F8F), success = Color(0xFF5F8F5F), warning = Color(0xFF8F8F5F), error = Color(0xFF8F5F5F), info = Color(0xFF8F7F5F), diffAdd = Color(0xFF5F8F5F), diffDelete = Color(0xFF8F5F5F)),
        darkPalette = OpenCodePalette(neutral = Color(0xFF3F3F3F), ink = Color(0xFFDCDCCC), primary = Color(0xFF8CD0D3), accent = Color(0xFF93E0E3), success = Color(0xFF7F9F7F), warning = Color(0xFFF0DFAF), error = Color(0xFFCC9393), info = Color(0xFFDFAF8F), diffAdd = Color(0xFF8FB28F), diffDelete = Color(0xFFDCA3A3))
    ),
)

internal fun appThemeDisplayName(theme: AppTheme): String =
    openCodeThemeSpecs.firstOrNull { it.theme == theme }?.displayName
        ?: when (theme) {
            AppTheme.LIGHT -> "Light"
            AppTheme.DARK -> "Dark"
            AppTheme.AMOLED -> "AMOLED"
            AppTheme.SYSTEM -> "System"
            else -> theme.name
        }

internal fun openCodeColorScheme(theme: AppTheme, dark: Boolean): ColorScheme? {
    val spec = openCodeThemeSpecs.firstOrNull { it.theme == theme } ?: return null
    val p = if (dark) spec.darkPalette else spec.lightPalette
    return paletteToColorScheme(p, dark)
}

private fun paletteToColorScheme(p: OpenCodePalette, dark: Boolean): ColorScheme {
    val white = Color(0xFFFFFFFF)
    val black = Color(0xFF000000)
    val ramp = if (dark) {
        listOf(
            p.neutral.mix(black, 0.32f),
            p.neutral.mix(black, 0.18f),
            p.neutral.mix(white, 0.055f),
            p.neutral.mix(white, 0.11f),
            p.neutral.mix(white, 0.17f)
        )
    } else {
        listOf(
            p.neutral.mix(white, 0.72f),
            p.neutral.mix(white, 0.36f),
            p.neutral.mix(black, 0.055f),
            p.neutral.mix(black, 0.12f),
            p.neutral.mix(black, 0.19f)
        )
    }
    return if (dark) {
        darkColorScheme(
            primary = p.primary,
            onPrimary = contrastOn(p.primary),
            primaryContainer = p.primary.mix(p.neutral, 0.74f),
            onPrimaryContainer = p.ink,
            secondary = p.accent,
            onSecondary = contrastOn(p.accent),
            secondaryContainer = p.accent.mix(p.neutral, 0.74f),
            onSecondaryContainer = p.ink,
            tertiary = p.info,
            onTertiary = contrastOn(p.info),
            tertiaryContainer = p.info.mix(p.neutral, 0.74f),
            onTertiaryContainer = p.ink,
            error = p.error,
            onError = contrastOn(p.error),
            errorContainer = p.error.mix(p.neutral, 0.74f),
            onErrorContainer = p.ink,
            background = p.neutral,
            onBackground = p.ink,
            surface = p.neutral,
            onSurface = p.ink,
            surfaceVariant = p.ink.mix(p.neutral, 0.90f),
            onSurfaceVariant = p.ink.mix(p.neutral, 0.55f),
            outline = p.ink.mix(p.neutral, 0.50f),
            outlineVariant = p.ink.mix(p.neutral, 0.82f),
            inverseSurface = p.ink,
            inverseOnSurface = p.neutral,
            inversePrimary = p.primary.mix(p.neutral, 0.35f),
            surfaceDim = p.neutral.mix(black, 0.24f),
            surfaceBright = p.neutral.mix(white, 0.14f),
            surfaceContainerLowest = ramp[0],
            surfaceContainerLow = ramp[1],
            surfaceContainer = ramp[2],
            surfaceContainerHigh = ramp[3],
            surfaceContainerHighest = ramp[4]
        )
    } else {
        lightColorScheme(
            primary = p.primary,
            onPrimary = contrastOn(p.primary),
            primaryContainer = p.primary.mix(p.neutral, 0.76f),
            onPrimaryContainer = p.ink,
            secondary = p.accent,
            onSecondary = contrastOn(p.accent),
            secondaryContainer = p.accent.mix(p.neutral, 0.76f),
            onSecondaryContainer = p.ink,
            tertiary = p.info,
            onTertiary = contrastOn(p.info),
            tertiaryContainer = p.info.mix(p.neutral, 0.76f),
            onTertiaryContainer = p.ink,
            error = p.error,
            onError = contrastOn(p.error),
            errorContainer = p.error.mix(p.neutral, 0.76f),
            onErrorContainer = p.ink,
            background = p.neutral,
            onBackground = p.ink,
            surface = p.neutral,
            onSurface = p.ink,
            surfaceVariant = p.ink.mix(p.neutral, 0.90f),
            onSurfaceVariant = p.ink.mix(p.neutral, 0.55f),
            outline = p.ink.mix(p.neutral, 0.50f),
            outlineVariant = p.ink.mix(p.neutral, 0.82f),
            inverseSurface = p.ink,
            inverseOnSurface = p.neutral,
            inversePrimary = p.primary.mix(p.neutral, 0.35f),
            surfaceDim = p.neutral.mix(black, 0.12f),
            surfaceBright = p.neutral.mix(white, 0.35f),
            surfaceContainerLowest = ramp[0],
            surfaceContainerLow = ramp[1],
            surfaceContainer = ramp[2],
            surfaceContainerHigh = ramp[3],
            surfaceContainerHighest = ramp[4]
        )
    }
}

private fun contrastOn(background: Color): Color {
    val luminance = 0.2126f * background.red + 0.7152f * background.green + 0.0722f * background.blue
    return if (luminance > 0.45f) Color(0xFF141414) else Color(0xFFFFFFFF)
}

private fun Color.mix(other: Color, fraction: Float): Color {
    val t = fraction.coerceIn(0f, 1f)
    return Color(
        red = red + (other.red - red) * t,
        green = green + (other.green - green) * t,
        blue = blue + (other.blue - blue) * t,
        alpha = 1f
    )
}