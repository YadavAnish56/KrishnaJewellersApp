"""Regenerate the README's original graphics: python tools/generate_readme_assets.py.

Requires Pillow (pip install Pillow). This is documentation tooling only;
Android Studio and the Android build do not need Python or Pillow.
"""
from pathlib import Path
import math
import os
from PIL import Image, ImageDraw, ImageFont

ROOT = Path(__file__).resolve().parents[1]
OUT = ROOT / "docs" / "assets"
OUT.mkdir(parents=True, exist_ok=True)
FONT_ROOT = Path(os.environ.get("WINDIR", "C:/Windows")) / "Fonts"


def font(size, serif=False, bold=False):
    names = (["georgia.ttf", "DejaVuSerif.ttf"] if serif else
             ["seguisb.ttf" if bold else "segoeui.ttf", "DejaVuSans.ttf"])
    for name in names:
        for location in (FONT_ROOT / name, Path(name)):
            try:
                return ImageFont.truetype(str(location), size)
            except OSError:
                pass
    raise RuntimeError("Install Georgia/Segoe UI or DejaVu fonts to regenerate graphics.")


BG = "#111318"
GOLD = "#d5b878"
WHITE = "#f6efe2"
MUTED = "#ada99f"
W, H = 1200, 450


def hero(phase):
    im = Image.new("RGB", (W, H), BG)
    d = ImageDraw.Draw(im)
    d.rectangle((18, 18, W-19, H-19), outline="#51452e", width=1)
    d.line((54, 62, 100, 62), fill=GOLD, width=2)
    d.text((115, 48), "THE DIGITAL SHOWROOM", font=font(17, bold=True), fill=GOLD)
    d.text((52, 102), "Krishna", font=font(75, serif=True), fill=WHITE)
    d.text((52, 187), "Jewellers", font=font(75, serif=True), fill=GOLD)
    phrases = ["Discover gold & silver collections.", "Explore every price detail.",
               "Save a design. Start a conversation."]
    position = phase * len(phrases)
    idx = int(position) % len(phrases)
    local = position % 1
    opacity = min(1, local / .15, (1-local) / .15)
    tone = tuple(round(a+(b-a)*opacity) for a,b in zip((17,19,24),(246,239,226)))
    d.text((56, 300 + int((1-opacity)*7)), phrases[idx], font=font(24), fill=tone)
    d.text((56, 384), "KOTLIN  /  JETPACK COMPOSE  /  ANDROID", font=font(16), fill=MUTED)
    for n in range(3):
        d.rounded_rectangle((56+n*42, 352, 82+n*42, 355), radius=1,
                            fill=GOLD if n == idx else "#3f3a30")
    # An original geometric jewel, drawn from primitives rather than a product mockup.
    cx, cy = 951, 221
    for radius in (145, 166):
        d.ellipse((cx-radius, cy-radius, cx+radius, cy+radius), outline="#3f372a", width=1)
    d.ellipse((cx-99, cy-52, cx+99, cy+146), outline=GOLD, width=5)
    d.ellipse((cx-89, cy-42, cx+89, cy+136), outline="#786443", width=1)
    diamond = [(cx-73,cy-72),(cx-43,cy-110),(cx+43,cy-110),(cx+73,cy-72),(cx,cy+4)]
    d.polygon(diamond, fill=BG, outline=GOLD, width=3)
    d.line((cx-73,cy-72,cx+73,cy-72), fill=GOLD,width=2)
    d.line((cx-43,cy-110,cx-28,cy-72,cx,cy+4,cx+28,cy-72,cx+43,cy-110),fill=GOLD,width=2)
    d.line((cx-28,cy-72,cx,cy-110,cx+28,cy-72),fill=GOLD,width=1)
    for offset in (0, .5):
        angle = (phase+offset)*2*math.pi
        x,y = cx+166*math.cos(angle),cy+166*math.sin(angle)
        d.ellipse((x-4,y-4,x+4,y+4),fill=GOLD)
    d.text((850, 409), "CRAFTED FOR ANDROID", font=font(13),fill=MUTED)
    return im


frames = [hero(i/72) for i in range(72)]
frames[8].save(OUT / "showroom-static.png", optimize=True)
frames[0].save(OUT / "showroom.gif", save_all=True, append_images=frames[1:],
               duration=100, loop=0, optimize=True, disposal=2)


def svg(content, height, name):
    (OUT / name).write_text(
        f'<svg xmlns="http://www.w3.org/2000/svg" width="1200" height="{height}" '
        f'viewBox="0 0 1200 {height}" role="img">{content}</svg>', encoding="utf-8")


svg('''<title>From screen to local storage</title>
<rect width="1200" height="280" rx="12" fill="#111318"/>
<g font-family="Segoe UI, Arial, sans-serif">
<text x="48" y="48" fill="#d5b878" font-size="16" letter-spacing="3">INSIDE THE APP</text>
<g fill="none" stroke="#6f5e3d" stroke-width="2">
<path d="M260 150H340 M540 150H620 M820 150H900"/>
<path d="m330 144 10 6-10 6m280-12 10 6-10 6m280-12 10 6-10 6"/>
</g>
<g fill="#1b1d21" stroke="#51452e">
<rect x="48" y="90" width="212" height="120" rx="8"/>
<rect x="340" y="90" width="200" height="120" rx="8"/>
<rect x="620" y="90" width="200" height="120" rx="8"/>
<rect x="900" y="90" width="252" height="120" rx="8"/>
</g>
<g fill="#f6efe2" font-size="24" text-anchor="middle">
<text x="154" y="137">Compose UI</text><text x="440" y="137">ViewModels</text>
<text x="720" y="137">Repository</text><text x="1026" y="137">Local storage</text></g>
<g fill="#ada99f" font-size="17" text-anchor="middle">
<text x="154" y="174">Screens &amp; navigation</text><text x="440" y="174">State &amp; actions</text>
<text x="720" y="174">Data operations</text><text x="1026" y="174">SQLite + DataStore</text></g>
<text x="48" y="253" fill="#ada99f" font-size="15">Observable state returns to the UI through Kotlin flows. Core demo data stays on the device.</text>
</g>''', 280, "architecture.svg")

svg('''<title>Clone, open, run</title><rect width="1200" height="180" rx="12" fill="#111318"/>
<g font-family="Segoe UI, Arial, sans-serif">
<g fill="#d5b878" font-size="42"><text x="44" y="81">01</text><text x="440" y="81">02</text><text x="836" y="81">03</text></g>
<g fill="#f6efe2" font-size="25"><text x="122" y="70">Clone</text><text x="518" y="70">Open</text><text x="914" y="70">Run</text></g>
<g fill="#ada99f" font-size="17"><text x="122" y="107">Get the source from GitHub</text><text x="518" y="107">Sync in Android Studio</text><text x="914" y="107">API 24+ device or emulator</text></g>
<path d="M400 42v94M796 42v94" stroke="#51452e"/>
<text x="44" y="156" fill="#ada99f" font-size="14">No backend setup required for the local demo.</text></g>''', 180, "quickstart.svg")

for label, value, name in [("APP", "1.0.2", "version"), ("ANDROID", "7.0+", "android"),
                            ("UI", "COMPOSE", "compose"), ("DATA", "LOCAL", "local")]:
    (OUT / f"badge-{name}.svg").write_text(
        f'<svg xmlns="http://www.w3.org/2000/svg" width="160" height="30" role="img">'
        f'<title>{label}: {value}</title><rect width="160" height="30" rx="5" fill="#25272c"/>'
        f'<path d="M65 0h90q5 0 5 5v20q0 5-5 5H65z" fill="#d5b878"/>'
        f'<g font-family="Arial,sans-serif" font-size="11" font-weight="bold" text-anchor="middle">'
        f'<text x="33" y="19" fill="#f6efe2">{label}</text>'
        f'<text x="112" y="19" fill="#111318">{value}</text></g></svg>',encoding="utf-8")

print("Generated README assets in docs/assets")
