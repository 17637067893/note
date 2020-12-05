set /p commit_log=insert message:
git status
git add -A
git commit -m "%commit_log%"
git push origin main
pause