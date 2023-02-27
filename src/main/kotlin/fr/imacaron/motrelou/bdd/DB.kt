package fr.imacaron.motrelou.bdd

abstract class DB<T> {
	abstract fun selectAll(): List<T>

	abstract fun selectById(id: Int): T

	abstract fun selectOne(): T

	abstract fun select(): T
}