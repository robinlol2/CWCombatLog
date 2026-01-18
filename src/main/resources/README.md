# CWCombatLog

CWCombatLog is a lightweight CombatLog / Combat Tag plugin for **Paper 1.21.x**.  
It prevents players from escaping fights by blocking commands during combat and punishes logging out while combat-tagged.

A key feature of this plugin is the built-in **PlaceholderAPI integration**, so you can show combat status in HUDs, scoreboards, menus, tablists, and more.

---

## Features

- ✅ Combat-tag triggers on **melee** damage
- ✅ Combat-tag triggers on **projectile** damage (e.g. bow/arrow)
- ✅ Configurable combat duration via `config.yml`
- ✅ Optional: tag the victim too (`include-victim`)
- ✅ Blocks commands while in combat (with an allowlist)
- ✅ Combat log punishment: logging out during combat = **death**
    - Includes a safe fallback (prevents abuse and avoids double-kill issues)
- ✅ PlaceholderAPI placeholder: shows if a player is in combat

---

## Requirements

- **Paper / Spigot 1.21.x** (tested on 1.21.4 and 1.21.10)
- **Java 21**
- **PlaceholderAPI** *(optional)* — only required if you want placeholders

---

## Installation

1. Download the plugin `.jar`
2. Place it in your server: `plugins/`
3. Restart the server
4. Edit the config: `plugins/CWCombatLog/config.yml`
5. (Optional) Install PlaceholderAPI if you want placeholder support
6. Reload the config with: `/cwcombatlog reload`

---

## Configuration (`config.yml`)

```yml
timer:
  seconds: 30
  include-victim: true

command-block:
  allow:
    - "/msg"
    - "/r"
    - "/tell"
    - "/help"
