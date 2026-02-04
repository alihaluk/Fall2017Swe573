# JULES_PLAN

## Mission
Move "Binged.TV" from Alpha to Beta by stabilizing the Core Gameplay Loop (Start -> Play -> Win).

## Current State Analysis
- **App Structure**: Standard Android app with multiple Activities and Fragments.
- **Start**: `LoginActivity` uses Trakt OAuth. Seems functional but untested by me.
- **Play**: `ShowsFragment` (Watched progress), `ExploreFragment` (Trending), `UpcomingFragment`, `WatchlistFragment` exist. Implementation uses basic Volley requests.
- **Win**: `EpisodeActivity` allows marking an episode as watched. Logic uses fragile string concatenation for JSON and has poor user feedback.
- **Issues**:
  - Code duplication for API headers.
  - Lack of loading indicators (poor UX).
  - Fragile JSON construction in `EpisodeActivity`.
  - Inefficient N+1 queries in `ShowsFragment`.

## Plan
1.  **Refactor API Layer**: Create `TraktApiClient` to handle headers and request queue interactions.
2.  **Fix Core Feature (Win Condition)**: Repair `EpisodeActivity`'s "Mark as Watched" functionality.
3.  **Improve UX**: Add `ProgressBar` to all list fragments.
4.  **Verify**: Build the project.

## Execution Log
- [ ] Created JULES_PLAN.md
- [ ] Refactored API calls
- [ ] Fixed EpisodeActivity
- [ ] Added Loading Indicators
- [ ] Verified Build
