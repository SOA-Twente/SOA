for /r %%i in (databases\*) do (
    kubectl apply -f %%i
)

