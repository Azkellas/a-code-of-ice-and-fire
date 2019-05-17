#Const SHOW_DEBUG = True

Imports System.Collections.Generic
Imports System.Diagnostics

Module ACOIF

    Public Enum BuildingType
        Hq
        Mine
        Tower
    End Enum

    Public Enum Team
        Fire
        Ice
    End Enum

    Private Const WIDTH As Integer = 12
    Private Const HEIGHT As Integer = 12

    Private Const MYSELF As Integer = 0
    Private Const OPPONENT As Integer = 1
    Private Const NEUTRAL As Integer = -1

    Private Const TRAIN_COST_LEVEL_1 As Integer = 10
    Private Const TRAIN_COST_LEVEL_2 As Integer = 20
    Private Const TRAIN_COST_LEVEL_3 As Integer = 30

    Sub Main()

        Dim game As New Game()
        game.Init()

        ' game loop
        While True
            game.Update()
            game.Solve()
            Console.WriteLine(game.Output.ToString())
        End While
    End Sub

    Public Class Game
        Public ReadOnly Buildings As New List(Of Building)

        Public ReadOnly Map(WIDTH, HEIGHT) As Tile
        Public Output As String = ""

        ' Not Usefull in Wood3
        Public MineSpots As New List(Of Position)

        Public MyGold As Integer
        Public MyIncome As Integer
        Public MyTeam As Team

        Public OpponentGold As Integer
        Public OpponentIncome As Integer
        Public Turn As Integer
        Public Units As New List(Of Unit)

        Public Function MyUnits() As List(Of Unit)
            Dim r As New List(Of Unit)
            For Each u As Unit In Units
                If u.IsOwned Then r.Add(u)
            Next
            Return r
        End Function
        Public Function OpponentUnits() As List(Of Unit)
            Dim r As New List(Of Unit)
            For Each u As Unit In Units
                If u.IsOpponent Then r.Add(u)
            Next
            Return r
        End Function

        Public Function MyHq() As Position
            If MyTeam = Team.Fire Then
                Return New Position(0, 0)
            Else
                Return New Position(11, 11)
            End If
        End Function

        Public Function OpponentHq() As Position
            If MyTeam = Team.Fire Then
                Return New Position(11, 11)
            Else
                Return New Position(0, 0)
            End If
        End Function

        Public MyPositions As New List(Of Position)
        Public OpponentPositions As New List(Of Position)
        Public NeutralPositions As New List(Of Position)

        Public Sub Init()
            For y As Integer = 0 To HEIGHT - 1
                For x As Integer = 0 To WIDTH - 1
                    Dim t As New Tile
                    t.Position = New Position(x, y)
                    Map(x, y) = t
                Next
            Next

            Dim numberMineSpots As Integer = CInt(Console.ReadLine())
            For i As Integer = 0 To numberMineSpots - 1
                Dim inputs() As String = Console.ReadLine().Split(" "c)
                MineSpots.Add(New Position(CInt(inputs(0)), CInt(inputs(1))))
            Next
        End Sub

        Public Sub Update()
            Units.Clear()
            Buildings.Clear()

            MyPositions.Clear()
            OpponentPositions.Clear()
            NeutralPositions.Clear()

            Output = ""

            ' --------------------------------------

            MyGold = CInt(Console.ReadLine())
            MyIncome = CInt(Console.ReadLine())
            OpponentGold = CInt(Console.ReadLine())
            OpponentIncome = CInt(Console.ReadLine())

            ' Read Map
            For y As Integer = 0 To HEIGHT - 1
                Dim line As String = Console.ReadLine()
                For x As Integer = 0 To WIDTH - 1
                    Dim c As String = line(x) & ""
                    Map(x, y).IsWall = c = "#"
                    Map(x, y).Active = "OX".Contains(c)
                    Map(x, y).Owner = If(c.ToLower() = "o", MYSELF, If(c.ToLower() = "x", OPPONENT, NEUTRAL))
                    Map(x, y).HasMineSpot = MineSpots.Contains(New Position(x, y))

                    Dim p As New Position(x, y)
                    If Map(x, y).IsOwned Then
                        MyPositions.Add(p)
                    ElseIf Map(x, y).IsOpponent Then
                        OpponentPositions.Add(p)
                    ElseIf Not Map(x, y).IsWall Then
                        NeutralPositions.Add(p)
                    End If
                Next
            Next

            ' Read Buildings
            Dim buildingCount As Integer = CInt(Console.ReadLine())
            For i As Integer = 0 To buildingCount - 1
                Dim inputs() As String = Console.ReadLine().Split(" "c)
                Dim b As New Building
                b.Owner = CInt(inputs(0))
                b.Type = DirectCast(CInt(inputs(1)), BuildingType)
                b.Position = New Position(CInt(inputs(2)), CInt(inputs(3)))
                Buildings.Add(b)
            Next

            ' Read Units
            Dim unitCount As Integer = CInt(Console.ReadLine())
            For i As Integer = 0 To unitCount - 1
                Dim inputs() As String = Console.ReadLine().Split(" "c)
                Dim u As New Unit
                u.Owner = CInt(inputs(0))
                u.Id = CInt(inputs(1))
                u.Level = CInt(inputs(2))
                u.Position = New Position(CInt(inputs(3)), CInt(inputs(4)))
                Units.Add(u)
            Next

            ' --------------------------------

            ' Get Team
            MyTeam = Team.Ice
            For Each b As Building In Buildings
                If b.IsOwned AndAlso b.IsHq AndAlso b.Position = New Position(0, 0) Then
                    MyTeam = Team.Fire : Exit For
                End If
            Next

            ' Usefull for symmetric AI
            If MyTeam = Team.Ice Then
                MyPositions.Reverse()
                OpponentPositions.Reverse()
                NeutralPositions.Reverse()
            End If

            ' --------------------------------

            ' Debug
            Debug()
        End Sub

        <Conditional("SHOW_DEBUG")>
        Public Sub Debug()
            Console.Error.WriteLine("Turn: " & Turn)
            Console.Error.WriteLine("My team: " & MyTeam)
            Console.Error.WriteLine("My gold: " & MyGold & " (+" & MyIncome & ")")
            Console.Error.WriteLine("Opponent gold: " & OpponentGold & " (+" & OpponentIncome & ")")

            Console.Error.WriteLine("=====")
            For Each b As Building In Buildings
                Console.Error.WriteLine(b)
            Next

            For Each u As Unit In Units
                Console.Error.WriteLine(u)
            Next
        End Sub

        '-----------------------------------------------------------
        'TODO Solve
        '-----------------------------------------------------------

        Public Sub Solve()
            ' Make sur the AI doesn't timeout
            Wait()

            MoveUnits()

            TrainUnits()

            Turn += 1
        End Sub

        Public Sub MoveUnits()
            ' Rush center
            Dim target As Position
            If MyTeam = Team.Fire Then
                target = New Position(5, 5)
            Else
                target = New Position(6, 6)
            End If

            If (Map(target.X, target.Y).IsOwned) Then Exit Sub

            For Each u As Unit In MyUnits()
                Move(u.Id, target)
            Next
        End Sub

        Public Sub TrainUnits()
            Dim target As Position
            If MyTeam = Team.Fire Then
                target = New Position(1, 0)
            Else
                target = New Position(10, 11)
            End If

            If MyGold >= TRAIN_COST_LEVEL_1 Then
                Train(1, target)
            End If
        End Sub

        Public Sub Wait()
            Output &= "WAIT;"
        End Sub

        Public Sub Train(level As Integer, position As Position)
            ' TODO: Handle upkeep
            Dim cost As Integer = 0
            Select Case level
                Case 1 : cost = TRAIN_COST_LEVEL_1
                Case 2 : cost = TRAIN_COST_LEVEL_2
                Case 3 : cost = TRAIN_COST_LEVEL_3
            End Select

            MyGold -= cost
            Output &= "TRAIN " & level & " " & position.X & " " & position.Y & ";"
        End Sub

        Public Sub Move(id As Integer, position As Position)
            ' TODO: Handle map change
            Output &= "MOVE " & id & " " & position.X & " " & position.Y & ";"
        End Sub

        ' TODO: Handle Build command
    End Class


    Public Class Unit
        Inherits Entity
        Public Id As Integer
        Public Level As Integer

        Public Overrides Function toString() As String
            Return "Unit => " & MyBase.toString() & " Id: " & Id & " Level: " & Level
        End Function
    End Class

    Public Class Building
        Inherits Entity

        Public Type As BuildingType

        ReadOnly Property IsHq As Boolean
            Get
                Return Type = BuildingType.Hq
            End Get
        End Property

        ReadOnly Property IsTower As Boolean
            Get
                Return Type = BuildingType.Tower
            End Get
        End Property

        ReadOnly Property IsMine As Boolean
            Get
                Return Type = BuildingType.Mine
            End Get
        End Property

        Public Overrides Function toString() As String
            Return "Building => " & MyBase.toString() & " Type: " & Type
        End Function
    End Class

    Public Class Entity
        Public Owner As Integer
        Public Position As Position

        ReadOnly Property IsOwned As Boolean
            Get
                Return Owner = MYSELF
            End Get
        End Property

        ReadOnly Property IsOpponent As Boolean
            Get
                Return Owner = OPPONENT
            End Get
        End Property

        ReadOnly Property X As Integer
            Get
                Return Position.X
            End Get
        End Property

        ReadOnly Property Y As Integer
            Get
                Return Position.Y
            End Get
        End Property

        Public Overrides Function toString() As String
            Return "Owner: " & Owner & " Position: " & Position.toString
        End Function

    End Class

    Public Class Tile
        Public Active As Boolean
        Public HasMineSpot As Boolean
        Public IsWall As Boolean

        Public Owner As Integer = NEUTRAL

        Public Position As Position
        ReadOnly Property X As Integer
            Get
                Return Position.X
            End Get
        End Property

        ReadOnly Property Y As Integer
            Get
                Return Position.Y
            End Get
        End Property

        Public Function IsOwned() As Boolean
            Return Owner = MYSELF
        End Function
        Public Function IsOpponent() As Boolean
            Return Owner = OPPONENT
        End Function
        Public Function IsNeutral() As Boolean
            Return Owner = NEUTRAL
        End Function
    End Class

    Public Structure Position
        Implements IEquatable(Of Position)

        Public X As Integer
        Public Y As Integer

        Sub New(X As Integer, Y As Integer)
            Me.X = X
            Me.Y = Y
        End Sub

        Public Overrides Function toString() As String
            Return "(" & X & "," & Y & ")"
        End Function

        Public Shared Operator =(obj1 As Position, obj2 As Position) As Boolean
            Return obj1.Equals(obj2)
        End Operator

        Public Shared Operator <>(obj1 As Position, obj2 As Position) As Boolean
            Return Not obj1.Equals(obj2)
        End Operator

        Public Overrides Function Equals(obj As Object) As Boolean
            If TypeOf obj Is Position Then
                Return Equals(DirectCast(obj, Position))
            Else
                Return False
            End If
        End Function

        Public Overloads Function Equals(other As Position) As Boolean Implements IEquatable(Of Position).Equals
            Return X = other.X AndAlso Y = other.Y
        End Function

        Public Function Dist(p As Position) As Double
            Return Math.Abs(X - p.X) + Math.Abs(Y - p.Y)
        End Function
    End Structure

End Module